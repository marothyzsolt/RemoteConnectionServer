package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.Message;
import com.topin.model.builder.ClientMessageBuilder;
import com.topin.model.builder.MessageBuilder;
import com.topin.model.command.LoginMessage;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private final ClientMessageDriver clientMessageDriver;
    private Socket client;
    private BufferedReader bufferedReader;

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
            // Wait for token from client
            String clientToken = this.waitToken();

            // Send a success status to just connected user
            this.sendStatusMessage(true);

            this.listen();
        } catch (Exception e) {
            System.out.println("Connection closed (" + this.client + ")");
        }
    }

    private String waitToken() throws IOException {
        System.out.println("Wait for login token for " + this.client.getInetAddress());

        String token = null;
        do {
            Message message = MessageBuilder.build(this.bufferedReader.readLine());
            if (message instanceof LoginMessage) {
                System.out.println("Token login with " + ((LoginMessage) message).getClientType() + " device with token: " + ((LoginMessage) message).getToken());
            } else {
                System.out.println("Client sent message before login: " + this.client.getInetAddress());
                this.sendStatusMessage(false);
            }
        } while (token == null);

        return token;
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
        new Thread(new ClientConnectionMessage(messageObject, this.clientMessageDriver)).start();

        this.sendStatusMessage(true);
    }

    private void sendStatusMessage(boolean status) {
        MessageBuilder builder = (MessageBuilder)
                new MessageBuilder("status")
                        .add("success", status);

        this.clientMessageDriver.send(builder.get());
    }
}
