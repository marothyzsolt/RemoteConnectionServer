package com.topin.services;

import com.topin.Utils;
import com.topin.driver.ClientMessageDriver;
import com.topin.model.ClientData;
import com.topin.model.ConnectedClient;
import com.topin.model.LoginClientList;
import com.topin.model.Message;
import com.topin.model.builder.ClientMessageBuilder;
import com.topin.model.builder.MessageBuilder;
import com.topin.model.command.LoginConnectMessage;
import com.topin.model.command.LoginMessage;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private final ClientMessageDriver clientMessageDriver;
    private Socket client;
    private BufferedReader bufferedReader;

    private String currentClientToken = null;

    public ClientConnection(Socket client) throws IOException {
        InputStream clientInputStream = client.getInputStream();

        this.client = client;
        this.bufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
        this.clientMessageDriver = new ClientMessageDriver(this.client);
    }

    public void run() {
        new Thread(new ClientMessageSender(this.clientMessageDriver)).start();
        //new Thread(new ScreenCapture(this.clientMessageDriver)).start();

        try {
            // Wait for login from client
            String x = this.waitLoginClient();
            Log.write(this).warn(x);
            //ConnectedClient.add(clientToken, this);

            // Wait for token from client (for authentication)
            String clientToken = this.waitToken();
            //ConnectedClient.add(clientToken, this);
            if (LoginClientList.get(clientToken) == null) {
                Log.write(this).error("The authentication was failed, the server drop the connection!");
                this.sendStatusMessage(false, "The authentication was failed, the server drop the connection!");
            } else {
                Log.write(this).info("Successful authentication! Starting to listen the client...");
            }

            // Send a success status to currently connected user
            //this.sendStatusMessage(true);

            this.listen();
        } catch (Exception e) {
            Log.write(this).info("Connection closed (" + this.client + ")");
        }
    }

    private String waitToken() throws IOException {
        Log.write(this).info("Wait for login token for " + this.client.getInetAddress());

        String token = null;
        do {
            Message message = MessageBuilder.build(this.bufferedReader.readLine());
            if (message instanceof LoginMessage) {
                Log.write(this).info("Successful Token login with [" + ((LoginMessage) message).getClientType() + "] device with token: " + ((LoginMessage) message).getToken());
                token = ((LoginMessage) message).getToken();
                this.sendStatusMessage(true);

                successfullyStoredClientToken((LoginMessage) message);
            } else {
                Log.write(this).info("Client sent message before login: " + this.client.getInetAddress() + " - " + (message != null ? message.toJson() : "NULL"));
                this.sendStatusMessage(false);
            }
        } while (token == null);

        return token;
    }

    private String waitLoginClient() throws IOException {
        Log.write(this).info("Wait for login client from " + this.client.getInetAddress());

        String username = null;
        String password = null;
        boolean successLogin = false;

        do {
            Message message = MessageBuilder.build(this.bufferedReader.readLine());
            if (message instanceof LoginConnectMessage) {
                Log.write(this).info("Client try login with username: " + ((LoginConnectMessage) message).getUsername());
                username = ((LoginConnectMessage) message).getUsername();
                password = ((LoginConnectMessage) message).getPassword();

                successLogin = this.checkLoginData(username, password);
                if (! successLogin) {
                    this.sendStatusMessage(false, "Incorrect login data");
                } else {
                    this.sendStatusMessage(true, "Successfully logged in");
                    this.successfullyClientLoginHandler(username, password);
                }
            } else {
                Log.write(this).info("Client sent message before login client: " + this.client.getInetAddress() + " - " + (message != null ? message.toJson() : "NULL"));
                this.sendStatusMessage(false);
            }
        } while (! successLogin);

        return username;
    }

    private void successfullyStoredClientToken(LoginMessage message) {
        ClientData currentClient = LoginClientList.get(this.currentClientToken);
        currentClient.setClientType(message.getClientType());

        System.out.println(message.getClientType());

        //if (message.getClientType().equals("server"))
    }

    private void successfullyClientLoginHandler(String username, String password) {
        if (this.currentClientToken == null || this.currentClientToken.length() != 32) {
            this.currentClientToken = Utils.randomGetNumericString(32);
        }
        this.sendStatusMessage(true, this.currentClientToken);
        ClientData clientData = new ClientData(username, password, this.currentClientToken, this);

        // Add if not exists
        LoginClientList.add(this.currentClientToken, clientData);
        Log.write(this).info("Store client data with token: " + this.currentClientToken);
    }

    private boolean checkLoginData(String username, String password) {
        return (username.equals("admin") && password.equals("admin"));
    }

    private void listen() throws IOException {
        String message;
        do {
            message = this.bufferedReader.readLine();

            Message messageObject = MessageBuilder.build(message);
            this.onMessage(messageObject);
        } while (message != null);
    }

    private void onMessage(Message messageObject) {
        // If the client login message, but already logged in
        if (messageObject != null && (messageObject.getType().equals("login") || messageObject.getType().equals("loginConnect"))) {
            ClientData clientData = LoginClientList.get(currentClientToken);
            if (clientData != null) {
                Log.write(this).info("The client tried to login when already logged in. The server sent the token to the client.");
                this.sendStatusMessage(true, "Already logged in");
                this.successfullyClientLoginHandler(clientData.getUsername(), clientData.getPassword());
            } else {
                Log.write(this).error("ERROR: The server got a login message, when the client already logged in, but the client not exists in [logged client] list of server.");
            }
        }

        new Thread(new ClientConnectionMessage(messageObject, this.clientMessageDriver)).start();

        //this.sendStatusMessage(true);
    }

    private void sendStatusMessage(boolean status) {
        this.sendStatusMessage(status, "null");
    }

    private void sendStatusMessage(boolean status, String message) {
        MessageBuilder builder = (MessageBuilder)
                new MessageBuilder("status")
                        .add("success", status)
                        .add("message", message);

        this.clientMessageDriver.send(builder.get());
    }

    public ClientMessageDriver getClientMessageDriver() {
        return this.clientMessageDriver;
    }
}
