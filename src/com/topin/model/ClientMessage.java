package com.topin.model;

public class ClientMessage {
    private String command;

    public ClientMessage(String message) {
        this.command = message;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "command='" + command + '\'' +
                '}';
    }
}
