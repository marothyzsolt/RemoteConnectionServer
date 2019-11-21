package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.ConnectedClient;
import com.topin.model.Message;


public class ClientConnectionMessage implements Runnable {
    private Message clientMessage;
    private ClientMessageDriver client;

    ClientConnectionMessage(Message clientMessage, ClientMessageDriver client) {
        this.clientMessage = clientMessage;
        this.client = client;
    }

    public void run() {
        System.out.println("Received message: " + this.clientMessage);

        if (this.clientMessage.getTargetToken() != null && this.clientMessage.getTargetToken().length() > 2) {
            ClientConnection targetClient = ConnectedClient.get(this.clientMessage.getTargetToken());
            if (targetClient == null) {
                System.out.println("The selected target token not exists: " + this.clientMessage.getTargetToken());
            } else {
                // Send message to target client
                targetClient.getClientMessageDriver().send(this.clientMessage.toString());
            }
        }
        //client.send(this.clientMessage);
    }
}
