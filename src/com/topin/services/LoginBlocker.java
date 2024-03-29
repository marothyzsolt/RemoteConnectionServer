package com.topin.services;

import com.topin.database.repositories.UserRepository;
import com.topin.model.ClientData;
import com.topin.model.LoginClientList;
import com.topin.model.Message;
import com.topin.model.builder.MessageBuilder;
import com.topin.model.command.*;
import java.io.IOException;

public class LoginBlocker {
    private ClientConnection clientConnection;

    public LoginBlocker(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public String waitToken(ClientData clientData) throws IOException {
        Log.write(this).info("Wait for login token for " + clientConnection.getClient().getInetAddress());

        String token = null;
        do {
            Message message = MessageBuilder.build(clientConnection.getBufferedReader().readLine());
            if (message instanceof LoginMessage) {
                if (! ((LoginMessage) message).getToken().equals(clientData.getToken())) {
                    Log.write(this).error("Client sent back wrong token. Excepted: " + clientData.getToken() + "; Get token: " + ((LoginMessage) message).getToken());
                } else {
                    if (((LoginMessage) message).getClientType().equals("server") && LoginClientList.findServerByUsername(clientData.getUsername()) != null) {
                        Log.write(this).error("Server try to connect one more time at the same time. Drop the old connection!");
                        LoginClientList.findServerByUsername(clientData.getUsername()).getClientConnection().drop();
                    } else if(((LoginMessage) message).getClientType().equals("client") && LoginClientList.findClientByUsername(clientData.getUsername()) != null) {
                        Log.write(this).error("Client try to connect one more time at the same time. Drop the old connection!");
                        LoginClientList.findClientByUsername(clientData.getUsername()).getClientConnection().drop();
                    }
                    LoginClientList.add(clientData.getToken(), clientData);

                    Log.write(this).info("Successful Token login with [" + ((LoginMessage) message).getClientType() + "] device with token: " + ((LoginMessage) message).getToken());
                    token = ((LoginMessage) message).getToken();
                    clientData.sendStatusMessage(true, "");

                    this.successfullyStoredClientToken((LoginMessage) message);
                }
            } else {
                Log.write(this).error("Client sent message before login: " + clientConnection.getClient().getInetAddress() + " - " + (message != null ? message.toJson() : "NULL"));
                clientConnection.getCurrentClientData().sendStatusMessage(false, "");
            }
        } while (token == null);

        return token;
    }

    public ClientData waitLoginClient() throws IOException {
        Log.write(this).info("Wait for login client from " + clientConnection.getClient().getInetAddress());

        String username = null;
        String password = null;
        boolean successLogin = false;

        String lastLine = clientConnection.getBufferedReader().readLine();
        if (lastLine == null) {
            this.clientConnection.drop();
            Log.write(this).error("Error while login, drop the connection");
            return null;
        }

        Message message = MessageBuilder.build(lastLine);
        if (message instanceof LoginConnectMessage) {
            Log.write(this).info("Client try login with username: " + ((LoginConnectMessage) message).getUsername());
            username = ((LoginConnectMessage) message).getUsername();
            password = ((LoginConnectMessage) message).getPassword();

            successLogin = this.checkLoginData(username, password);
            if (! successLogin) {
                Log.write(this).info("Client ["+username+"] login error. Incorrect login data");
                this.clientConnection.getClientMessageDriver().send(new StatusMessage(true, "Incorrect login data"));
                return null;
            } else {
                Log.write(this).info("Client ["+username+"] successfully logged in.");

                // Send the first return message to client. It's contains the success message
                this.clientConnection.getClientMessageDriver().send(new StatusMessage(true, "Successfully logged in"));

                // Send the second return message to client. It's contains the client login token
                return this.clientConnection.successfullyClientLoginHandler(username, password);
            }
        } else {
            Log.write(this).info("Client sent message before login client: " + clientConnection.getClient().getInetAddress() + " - " + (message != null ? message.toJson() : "NULL"));
            clientConnection.getCurrentClientData().sendStatusMessage(false, "");
        }

        return null;
    }

    private void successfullyStoredClientToken(LoginMessage message) {
        ClientData currentClient = LoginClientList.get(clientConnection.getCurrentClientToken());
        currentClient.setClientType(message.getClientType());

        ClientData targetClient;

        if (message.getClientType().equals("server")) { // Server connected
            targetClient = this.onServerConnected(currentClient);
        } else { // Client Connected
            targetClient = this.onClientConnected(currentClient);
        }

        clientConnection.setTargetClientData(targetClient);
    }

    private boolean checkLoginData(String username, String password) {
        UserRepository userRepository = new UserRepository();
        userRepository.loginUser(username, password);

        return ! userRepository.isEmpty();
    }

    private ClientData onClientConnected(ClientData currentClient) {
        ClientData targetClient;
        targetClient = LoginClientList.findServerByUsername(currentClient.getUsername());
        if (targetClient != null) {
            InitMessage oldInit = targetClient.getInitMessage();
            if (oldInit != null) { // Send existing init data (from server) to client
                currentClient.getClientConnection().getClientMessageDriver().send(oldInit);
            } else {
                targetClient.sendRequest("init");
            }
        } else {
            currentClient.sendMessage(new NoTargetServerMessage());
        }

        return targetClient;
    }

    private ClientData onServerConnected(ClientData currentClient) {
        ClientData targetClient;
        targetClient = LoginClientList.findClientByUsername(currentClient.getUsername());

        return targetClient;
    }

    boolean checkOnLoginMessageByLoggedInClient(Message messageObject) {
        if (messageObject != null && (messageObject.getType().equals("login") || messageObject.getType().equals("loginConnect"))) {
            ClientData clientData = LoginClientList.get(this.clientConnection.getCurrentClientToken());
            if (clientData != null) {
                Log.write(this).info("The client tried to login when already logged in. The server sent the token to the client.");
                this.clientConnection.getCurrentClientData().sendStatusMessage(true, "Already logged in");
                this.clientConnection.successfullyClientLoginHandler(clientData.getUsername(), clientData.getPassword());
            } else {
                Log.write(this).error("ERROR: The server got a login message, when the client already logged in, but the client not exists in [logged client] list of server.");
            }
            return true;
        }
        return false;
    }
}
