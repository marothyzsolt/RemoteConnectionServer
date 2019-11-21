package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.Message;

import java.net.Socket;

public class ClientConnectionMessage implements Runnable {
    private Message clientMessage;
    private ClientMessageDriver client;

    ClientConnectionMessage(Message clientMessage, ClientMessageDriver client) {
        this.clientMessage = clientMessage;
        this.client = client;
    }

    public void run() {
        System.out.println("Sending:" + this.clientMessage);
        //client.send(this.clientMessage);
    }
}
