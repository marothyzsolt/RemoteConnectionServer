package com.topin.model;

import java.util.HashMap;
import java.util.Map;

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

    public static ClientData findServerByUsername(String username) {
        return findByUsername("server", username);
    }

    public static ClientData findClientByUsername(String username) {
        return findByUsername("client", username);
    }

    public static ClientData findByUsername(String type, String username) {
        for (Map.Entry<String, ClientData> entry : clientList.entrySet()) {
            if (
                    entry.
                            getValue().
                            getClientType().
                            equals(type) &&
                            entry.
                                    getValue().
                                    getUsername().
                                    equals(username)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
