package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.ConnectedClient;
import com.topin.model.LoginClientList;
import com.topin.model.Message;
import com.topin.model.command.NoTargetServerMessage;
import com.topin.model.messagers.BaseMessager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class ClientConnectionMessage implements Runnable {
    private Message clientMessage;
    private ClientConnection baseClientConnection;

    ClientConnectionMessage(Message clientMessage, ClientConnection baseClientConnection) {
        this.clientMessage = clientMessage;
        this.baseClientConnection = baseClientConnection;
    }

    public void run() {

        if (this.clientMessage == null) {
           // Maybe the clientMessage is NULL, the connection has been closed:
            if (! this.baseClientConnection.getClient().isConnected()) {
                this.baseClientConnection.drop();
                LoginClientList.remove(this.baseClientConnection.getCurrentClientToken());
                Log.write(this).warn("Got a null message, and the client has been disconnected. Remove old data");
            }
        } else {
            if(clientMessage.getType().equals("ping")) {
                return;
            }

            Log.write(this).info("Received message: " + this.clientMessage.toOutput());

            this.generateCallCustomClass();

            if (this.clientMessage.getTargetToken() != null && this.clientMessage.getTargetToken().length() > 0) {
                this.baseClientConnection.tryToAssociateTargetClientData();

                ClientConnection targetClient = null;
                if (this.clientMessage.getTargetToken().equals("1")) {
                    if (this.baseClientConnection.getTargetClientData() == null) {
                        Log.write(this).warn("Received message, but the target is NOT specified!");
                    } else {
                        targetClient = this.baseClientConnection.getTargetClientData().getClientConnection();
                        Log.write(this).info("Received message send to target client token: " + targetClient.getCurrentClientToken() + " - " + this.clientMessage.toOutput());
                    }
                }
                //ClientConnection targetClient = ConnectedClient.get(this.clientMessage.getTargetToken());
                if (targetClient == null) {
                    Log.write(this).error("The selected target token not exists: " + this.clientMessage.getTargetToken());
                    baseClientConnection.getCurrentClientData().sendMessage(new NoTargetServerMessage());
                } else {
                    // Send message to target client
                    targetClient.getClientMessageDriver().send(this.clientMessage);
                }
            }
            //client.send(this.clientMessage);

        }
    }

    private void generateCallCustomClass() {
        String uppercaseClass = this.clientMessage.getType().substring(0,1).toUpperCase() + this.clientMessage.getType().substring(1).toLowerCase();
        String className = "com.topin.model.messagers.Messager" + uppercaseClass;

        try {
            Class myClass = Class.forName(className);
            Constructor constructor = myClass.getConstructor(Message.class, ClientConnection.class);
            BaseMessager messager = (BaseMessager) constructor.newInstance(this.clientMessage, this.baseClientConnection);
            messager.handle();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Log.write(this).error("ERROR: Error while creating Messager class...");
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) { }
    }
}
