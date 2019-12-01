package com.topin.driver;

import com.topin.model.Message;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract public class BaseDriver {
    protected BlockingQueue driver =  new ArrayBlockingQueue(32);
    protected Socket client;

    public BaseDriver(Socket client) {
        this.client = client;
    }

    public void send(Message o) {
        try {
            this.driver.put(o);
        } catch (Exception ignored) {}
    }

    public Message waitForDriver() throws InterruptedException {
        return (Message) this.driver.take();
    }

    public Socket getClient() {
        return client;
    }
}
