package com.topin.model;

import com.topin.services.ClientConnection;

import java.util.HashMap;

public class ConnectedClient {
    private static HashMap<String, ClientConnection> clientList = new HashMap<>();

    public static void add(String token, ClientConnection clientConnection) {
        clientList.put(token, clientConnection);
    }

    public static void remove(String token) {
        clientList.remove(token);
    }

    public static ClientConnection get(String token) {
        return clientList.get(token);
    }
}
