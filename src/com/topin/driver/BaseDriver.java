package com.topin.driver;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract public class BaseDriver {
    protected BlockingQueue driver =  new ArrayBlockingQueue(32);
    protected Socket client;

    public BaseDriver(Socket client) {
        this.client = client;
    }

    public void send(Object o) {
        try {
            this.driver.put(o);
        } catch (Exception ignored) {}
    }

    public Object waitForDriver() throws InterruptedException {
        return this.driver.take();
    }

    public Socket getClient() {
        return client;
    }
}
