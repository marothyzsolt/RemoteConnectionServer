package com.topin;

import com.topin.services.ClientConnection;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting server...");
        try {
            ServerSocket server = new ServerSocket(7777);
            while (true) {
                Socket client = server.accept();
                new Thread(new ClientConnection(client)).start();
                System.out.println("Client connected: " + client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
