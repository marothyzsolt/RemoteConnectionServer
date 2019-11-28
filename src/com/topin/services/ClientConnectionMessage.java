package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.ConnectedClient;
import com.topin.model.Message;


public class ClientConnectionMessage implements Runnable {
    private Message clientMessage;
    private ClientConnection baseClientConnection;

    ClientConnectionMessage(Message clientMessage, ClientConnection baseClientConnection) {
        this.clientMessage = clientMessage;
        this.baseClientConnection = baseClientConnection;
    }

    public void run() {
        Log.write(this).info("Received message: " + this.clientMessage);

        if (this.clientMessage == null) {
           Log.write(this).error("ERROR: clientMessage is NULL <<<<<<<<<<<<<<<<<<<");
        } else {

            if (this.clientMessage.getTargetToken() != null && this.clientMessage.getTargetToken().length() > 0) {
                ClientConnection targetClient = null;
                if (this.clientMessage.getTargetToken().equals("1")) {
                    if (this.baseClientConnection.getTargetClientData() == null) {
                        Log.write(this).warn("Received message, but the target is NOT specified!");
                    } else {
                        targetClient = this.baseClientConnection.getTargetClientData().getClientConnection();
                        Log.write(this).info("Received message send to target client token: " + targetClient.getCurrentClientToken());
                    }
                }
                //ClientConnection targetClient = ConnectedClient.get(this.clientMessage.getTargetToken());
                if (targetClient == null) {
                    Log.write(this).error("The selected target token not exists: " + this.clientMessage.getTargetToken());
                } else {
                    // Send message to target client
                    targetClient.getClientMessageDriver().send(this.clientMessage.toString());
                }
            }
            //client.send(this.clientMessage);

        }
    }
}
