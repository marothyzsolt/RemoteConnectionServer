package com.topin.services;

import com.topin.Utils;
import com.topin.driver.ClientMessageDriver;
import com.topin.model.ClientData;
import com.topin.model.LoginClientList;
import com.topin.model.Message;
import com.topin.model.builder.MessageBuilder;
import com.topin.model.command.RequestMessage;
import com.topin.model.command.StatusMessage;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private final ClientMessageDriver clientMessageDriver;
    private Socket client;
    private BufferedReader bufferedReader;
    private String currentClientToken = null;
    private ClientData targetClientData;
    private LoginBlocker loginBlocker;

    public ClientConnection(Socket client) throws IOException {
        InputStream clientInputStream = client.getInputStream();

        this.client = client;
        this.bufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
        this.clientMessageDriver = new ClientMessageDriver(this.client);
    }

    public void run() {
        new Thread(new ClientMessageSender(this.clientMessageDriver, this)).start();
        //new Thread(new ScreenCapture(this.clientMessageDriver)).start();

        this.loginBlocker = new LoginBlocker(this);

        try {
            // Wait for login from client
            String x = loginBlocker.waitLoginClient();
            //Log.write(this).warn(x);
            //ConnectedClient.add(clientToken, this);

            // Wait for token from client (for authentication)
            String clientToken = loginBlocker.waitToken();
            //ConnectedClient.add(clientToken, this);
            if (LoginClientList.get(clientToken) == null) {
                Log.write(this).error("The authentication was failed, the server drop the connection!");
                this.getCurrentClientData().sendStatusMessage(false, "The authentication was failed, the server drop the connection!");

                this.client.close();
            } else {
                Log.write(this).info("Successful authentication! Starting to listen the client: " + this.getCurrentClientData());

                this.listen();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.write(this).error(e.getMessage());

            if (this.getCurrentClientData() != null) {
                Log.write(this).info("Connection closed (" + this.client + ") [" + this.getCurrentClientData() + "]");
            } else {
                Log.write(this).info("Connection closed (" + this.client + ")");
            }
            if (this.getCurrentClientData() != null) {
                if (this.getCurrentClientData().getClientType().equals("server")) {
                    if (LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()) != null) {
                        LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()).getClientConnection().setTargetClientData(null);
                    }
                } else {
                    if (LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername()) != null) {
                        LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername()).getClientConnection().setTargetClientData(null);
                    }
                }
            }

            if (this.currentClientToken != null) {
                Log.write(this).info("Remove connected client from ClientList: " + this.currentClientToken);
                LoginClientList.remove(this.currentClientToken);
            }
        }
    }

    public void tryToAssociateTargetClientData() {
        if (this.targetClientData == null) {
            if (this.getCurrentClientData().getClientType().equals("server")) {
                this.targetClientData = LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername());
            } else {
                this.targetClientData = LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername());
            }
        }
    }

    public void successfullyClientLoginHandler(String username, String password) {
        if (this.currentClientToken == null || this.currentClientToken.length() != 32) {
            this.currentClientToken = Utils.randomGetNumericString(32);
        }
        ClientData clientData = new ClientData(username, password, this.currentClientToken, this);

        // Add if not exists
        LoginClientList.add(this.currentClientToken, clientData);

        // After stored the client data into the LoginClientList, the center send back the success message to client.
        this.getCurrentClientData().sendStatusMessage(true, this.currentClientToken);
        Log.write(this).info("Store client [" + username + " - " + clientData.getClientType() +"] data with token: " + this.currentClientToken);
    }

    private void listen() throws IOException {
        String message;
        do {
            message = this.bufferedReader.readLine();

            // If target client not defined yet, it's try to define it, and store
            tryToAssociateTargetClientData();

            //this.getClientMessageDriver().send(new RequestMessage("init", null));

            Message messageObject = MessageBuilder.build(message);
            this.onMessage(messageObject);
        } while (message != null);
    }

    private void onMessage(Message messageObject) {
        // If the client login message, but already logged in
        if (this.loginBlocker.checkOnLoginMessageByLoggedInClient(messageObject)) {
            return; // Because the user sent a login/loginConnect message, and it should not handle by the main message listener
        }

        new Thread(new ClientConnectionMessage(messageObject, this)).start();
    }

    public ClientData getCurrentClientData() {
        return LoginClientList.get(this.currentClientToken);
    }

    public ClientMessageDriver getClientMessageDriver() {
        return this.clientMessageDriver;
    }

    public ClientData getTargetClientData() {
        return targetClientData;
    }

    public String getCurrentClientToken() {
        return currentClientToken;
    }

    public void setTargetClientData(ClientData targetClientData) {
        this.targetClientData = targetClientData;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public Socket getClient() {
        return client;
    }
}
