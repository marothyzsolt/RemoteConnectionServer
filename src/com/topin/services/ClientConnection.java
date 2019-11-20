package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.Message;
import com.topin.model.builder.MessageBuilder;

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
        new Thread(new ScreenCapture(this.clientMessageDriver)).start();

        // Send a success status to just connected user
        this.sendStatusMessage(true);

        try {
            this.listen();
        } catch (Exception e) {
            System.out.println("Connection closed (" + this.client + ")");
        }
    }

    private void listen() throws IOException, InterruptedException {
        String message;
        do {
            message = bufferedReader.readLine();

            Message messageObject = MessageBuilder.build(message);
            this.onMessage(messageObject);
        } while (message != null);
    }

    private void onMessage(Message messageObject) {
        new Thread(new ClientConnectionMessage(messageObject)).start();

        this.sendStatusMessage(true);
    }

    private void sendStatusMessage(boolean status) {
        MessageBuilder builder = (MessageBuilder)
                new MessageBuilder("status")
                        .add("success", status);

        this.clientMessageDriver.send(builder.get());
    }
}
