package com.topin.model;

import com.topin.model.command.InitMessage;
import com.topin.model.command.RequestMessage;
import com.topin.model.command.StatusMessage;
import com.topin.services.ClientConnection;
import org.json.JSONObject;

import java.util.HashMap;

public class ClientData {
    private String username;
    private String password;
    private String token;
    private ClientConnection clientConnection;
    private String clientType;
    private InitMessage initMessage;

    public ClientData(String username, String password, String token, ClientConnection clientConnection) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.clientConnection = clientConnection;
    }

    public void sendStatusMessage(boolean status, String message) {
        sendMessage(new StatusMessage(status, message));
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
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

    public InitMessage getInitMessage() {
        return initMessage;
    }

    public void setInitMessage(InitMessage initMessage) {
        this.initMessage = initMessage;
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }

    public void sendRequest(RequestMessage reqRequestMessage) {
        sendMessage(reqRequestMessage);
    }

    public void sendRequest(String request) {
        sendMessage(new RequestMessage(request, null));
    }

    public void sendRequest(String request, JSONObject parameters) {
        sendMessage(new RequestMessage(request, parameters.toString()));
    }

    public void sendMessage(Message message) {
        this.getClientConnection().getClientMessageDriver().send(message.toJson());
    }
}
