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
       // new Thread(new ScreenCapture(this.clientMessageDriver)).start();

        try {
            do {
                this.listen();
            } while (this.bufferedReader.readLine() != null);
        } catch (Exception e) {
            System.out.println("Connection closed (" + this.client + ")");
        }
    }

    private void listen() throws IOException, InterruptedException {
        do {
            String message = bufferedReader.readLine();

            Message messageObject = MessageBuilder.build(message);
            this.onMessage(messageObject);

        } while (bufferedReader.readLine() != null);
    }

    private void onMessage(Message messageObject) {
        new Thread(new ClientConnectionMessage(messageObject)).start();

        MessageBuilder builder = (MessageBuilder)
                new MessageBuilder("status")
                        .add("success", true);

        this.clientMessageDriver.send(builder.get());
    }
}
