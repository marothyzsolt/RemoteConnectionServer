package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.ClientMessage;
import org.json.JSONObject;

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
            //this.clientMessageDriver.send("Teszt 12321");

            String message = bufferedReader.readLine();
            this.onMessage(new ClientMessage(message));

        } while (bufferedReader.readLine() != null);
    }

    private void onMessage(ClientMessage clientMessage) throws InterruptedException {
        new Thread(new ClientConnectionMessage(clientMessage)).start();

        String jsonString = new JSONObject()
                .put("success", true)
                .toString();
        this.clientMessageDriver.send(jsonString);
    }
}
