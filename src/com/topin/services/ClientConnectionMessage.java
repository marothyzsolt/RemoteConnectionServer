package com.topin.services;

import com.topin.model.Message;

public class ClientConnectionMessage implements Runnable {
    private Message clientMessage;

    ClientConnectionMessage(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    public void run() {
        System.out.println(this.clientMessage);
    }
}
