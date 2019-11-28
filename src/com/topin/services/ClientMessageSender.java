package com.topin.services;

import com.topin.driver.ClientMessageDriver;

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
                Object o = this.clientMessageDriver.waitForDriver();
                this.onMessage(o);
                //System.out.println(o);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void onMessage(Object o) throws IOException {
        //System.out.println(this.clientMessageDriver.getClient());
        //System.out.println(o);


        StringReader stringReader = new StringReader(o.toString());

        System.out.println("Sending: " + o);
        if (this.clientConnection.getCurrentClientData() != null) {
            Log.write(this)
                    .info("Sending to [" + this.clientConnection.getCurrentClientData().getUsername() + "] [" + this.clientConnection.getCurrentClientData().getClientType() + "]: " + o);
        } else if (this.clientConnection.getCurrentClientToken() != null) {
            Log.write(this)
                    .info("Sending to [" + this.clientConnection.getCurrentClientToken() + "]: " + o);
        } else {
            Log.write(this)
                    .info("Sending to [" + this.clientMessageDriver.getClient().toString() + "]: " + o);
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
