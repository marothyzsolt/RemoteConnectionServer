package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.ClientData;
import com.topin.model.LoginClientList;
import com.topin.model.Message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

public class ClientMessageSender implements Runnable {

    private ClientMessageDriver clientMessageDriver;
    private ClientConnection clientConnection;

    public ClientMessageSender(ClientMessageDriver clientMessageDriver, ClientConnection clientConnection) {
        this.clientMessageDriver = clientMessageDriver;
        this.clientConnection = clientConnection;
    }

    public void run() {
        try {
            while (true) {
                Message o = this.clientMessageDriver.waitForDriver();
                this.onMessage(o);
                //System.out.println(o);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            Log.write(this).error(e.getMessage());
        }
    }

    private void onMessage(Message o) throws IOException {
        //System.out.println(this.clientMessageDriver.getClient());
        //System.out.println(o);


        StringReader stringReader = new StringReader(o.toString());

        if (this.clientConnection.getClient().isClosed() || ! this.clientConnection.getClient().isConnected()) {
            if (this.clientConnection.getCurrentClientData() != null) {
                Log.write(this).error("SEND-ERROR: Tried to send data to " + this.clientConnection.getCurrentClientData().getClientType() + " but the socket is already closed!");
                Log.write(this).error("SEND-ERROR: Remove the socket closed client/server from the LoginClientList");
                Log.write(this).info("SEND-INFO: Sending failed, client connection has been closed. Try to find same client which has active connection with the server.");
                ClientData oldClientData = this.clientConnection.getCurrentClientData();
                LoginClientList.remove(this.clientConnection.getCurrentClientToken());

                ClientData newClientData = LoginClientList.findByUsername(oldClientData.getClientType(), oldClientData.getUsername());
                if (newClientData != null) {
                    newClientData.getClientConnection().getClientMessageDriver().send(o);
                    Log.write(this).info("SEND-INFO: The failed message redirected to correct end point.");
                }
            }

            // Drop the old connection
            this.clientConnection.drop();

            return;
        }

///        System.out.println("Sending: " + o);
        if (this.clientConnection.getCurrentClientData() != null) {
            Log.write(this)
                    .info("Sending to [" + this.clientConnection.getCurrentClientData().getUsername() + "] " +
                            "[" + this.clientConnection.getCurrentClientData().getClientType() + "]: " + o.toOutput());
        } else if (this.clientConnection.getCurrentClientToken() != null) {
            Log.write(this)
                    .info("Sending to [" + this.clientConnection.getCurrentClientToken() + "]: " + o.toOutput());
        } else {
            Log.write(this)
                    .info("Sending to [" + this.clientMessageDriver.getClient().toString() + "]: " + o.toOutput());
        }

        OutputStream outputStream = new BufferedOutputStream(this.clientMessageDriver.getClient().getOutputStream());
        int c;
        while ((c = stringReader.read()) != -1) {
            outputStream.write(c);
        }
        outputStream.write((int) '\n');
        outputStream.flush();

    }
}
