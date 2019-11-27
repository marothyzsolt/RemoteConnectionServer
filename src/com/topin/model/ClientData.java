package com.topin.model;

import com.topin.services.ClientConnection;

import java.util.HashMap;

public class ClientData {
    private String username;
    private String password;
    private String token;
    private ClientConnection clientConnection;

    public ClientData(String username, String password, String token, ClientConnection clientConnection) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.clientConnection = clientConnection;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
