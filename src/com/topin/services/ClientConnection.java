package com.topin.services;

import com.topin.Utils;
import com.topin.driver.ClientMessageDriver;
import com.topin.model.ClientData;
import com.topin.model.LoginClientList;
import com.topin.model.Message;
import com.topin.model.builder.MessageBuilder;
import com.topin.model.command.NoTargetServerMessage;
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

    private boolean drop = false;

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
            ClientData clientData = loginBlocker.waitLoginClient();
            if (clientData == null) {
                this.drop();
                if (this.currentClientToken != null) {
                    LoginClientList.remove(this.currentClientToken);
                }
                this.client.close();
                return;
            }
            //Log.write(this).warn(x);
            //ConnectedClient.add(clientToken, this);

            // Wait for token from client (for authentication)
            String clientToken = loginBlocker.waitToken(clientData);
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
           // e.printStackTrace();
           // Log.write(this).error(e.getMessage());

            if (this.getCurrentClientData() != null) {
                Log.write(this).info("Connection closed (" + this.client + ") [" + this.getCurrentClientData() + "]");
            } else {
                Log.write(this).info("Connection closed (" + this.client + ")");
            }

            if (this.currentClientToken != null) {
                if (this.getCurrentClientData() != null) { // The client/server already stored in center
                    // Now must remove the disconnected server/client data from the center
                    // And the server's client, or client's server target token id

                    ClientData currentClient = this.getCurrentClientData();

                    if (currentClient.getClientType().equals("server")) { // The disconnected user was a SERVER
                        ClientData anotherUser = LoginClientList.findClientByUsername(currentClient.getUsername());
                        if (anotherUser != null) {
                            anotherUser.getClientConnection().getCurrentClientData().sendMessage(new NoTargetServerMessage());
                            anotherUser.getClientConnection().targetClientData = null;
                        }
                        System.out.println("The SERVER has been disconnected");
                    }

                    if (currentClient.getClientType().equals("client")) { // The disconnected user was a CLIENT
                        System.out.println("The CLIENT has been disconnected");
                    }

                /*if (this.getCurrentClientData().getClientType().equals("server")) {
                    //System.out.println(LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()));
                    //System.out.println(this.currentClientToken);

                    if (LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()) != null) {
                        LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()).getClientConnection()
                                .getTargetClientData().sendMessage(new NoTargetServerMessage());

                        LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()).getClientConnection().setTargetClientData(null);

                    }
                } else {
                    if (LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername()) != null) {
                        LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername()).getClientConnection().setTargetClientData(null);
                    }
                }*/

                    Log.write(this).info("Remove connected client from ClientList: " + this.currentClientToken);
                    LoginClientList.remove(this.currentClientToken);
                    System.out.println(LoginClientList.getClientList());
                }
            }

            try {
                this.client.close();
            } catch (IOException ignored) { }
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

    public ClientData successfullyClientLoginHandler(String username, String password) {
        if (this.currentClientToken == null || this.currentClientToken.length() != 32) {
            this.currentClientToken = Utils.randomGetNumericString(32);
        }
        ClientData clientData = new ClientData(username, password, this.currentClientToken, this);

        // The center send back the success message to client.
        clientData.sendStatusMessage(true, this.currentClientToken);

        return clientData;
    }

    private void listen() throws IOException {
        String message;
        do {
            System.out.println("Thread count: " + Thread.activeCount());

            message = this.bufferedReader.readLine();

            // If target client not defined yet, it's try to define it, and store
            tryToAssociateTargetClientData();

            //this.getClientMessageDriver().send(new RequestMessage("init", null));

            if (message == null) {
                Log.write(this).error("Got NULL message, drop the connection: " + this.currentClientToken + "["+this.getCurrentClientData().getClientType()+"]");
                if (this.getCurrentClientData().getClientType().equals("client") && LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername()) != null) {
                    (LoginClientList.findServerByUsername(this.getCurrentClientData().getUsername()).getClientConnection().targetClientData) = null;
                    Log.write(this).info("Remove old target");
                } else if (this.getCurrentClientData().getClientType().equals("server") && LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()) != null) {
                    (LoginClientList.findClientByUsername(this.getCurrentClientData().getUsername()).getClientConnection().targetClientData) = null;
                    Log.write(this).info("Remove old target");
                }
                LoginClientList.remove(this.getCurrentClientToken());
                this.drop();
            } else {
                Message messageObject = MessageBuilder.build(message);
                this.onMessage(messageObject);
            }
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

    public void drop() {
        this.drop = true;
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
