package com.topin.services;

import com.topin.model.ClientMessage;

public class ClientConnectionMessage implements Runnable {
    private ClientMessage clientMessage;

    ClientConnectionMessage(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }

    public void run() {
        System.out.println(this.clientMessage);
    }
}
