package com.topin.model;

import com.topin.services.ClientConnection;

import java.util.HashMap;

public class LoginClientList {
    private static HashMap<String, ClientData> clientList = new HashMap<>();

    public static void add(String token, ClientData clientData) {
        if (! clientList.containsKey(token)) {
            clientList.put(token, clientData);
        }
    }

    public static void remove(String token) {
        clientList.remove(token);
    }

    public static ClientData get(String token) {
        return clientList.get(token);
    }
}
