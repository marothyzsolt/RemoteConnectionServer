package com.topin.services;

import com.topin.driver.ClientMessageDriver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

public class ClientMessageSender implements Runnable {

    private ClientMessageDriver clientMessageDriver;

    public ClientMessageSender(ClientMessageDriver clientMessageDriver) {
        this.clientMessageDriver = clientMessageDriver;
    }

    public void run() {
        try {
            while (true) {
                Object o = this.clientMessageDriver.waitForDriver();
                this.onMessage(o);
                //System.out.println(o);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void onMessage(Object o) throws IOException {
        //System.out.println(this.clientMessageDriver.getClient());
        //System.out.println(o);


        StringReader stringReader = new StringReader(o.toString());


        OutputStream outputStream = new BufferedOutputStream(this.clientMessageDriver.getClient().getOutputStream());
        outputStream = new BufferedOutputStream(this.clientMessageDriver.getClient().getOutputStream());
        int c;
        while ((c = stringReader.read()) != -1) {
            outputStream.write(c);
            System.out.println(c);
        }
        outputStream.write((int) '\n');
        outputStream.flush();

    }
}
