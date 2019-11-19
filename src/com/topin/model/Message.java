package com.topin.model;

import com.topin.model.contracts.MessageContract;

abstract public class Message implements MessageContract {
    private String type;

    public Message(String type) {
        this.type = type;
    }

    public String toString() {
        String json = this.toJson();
        if (json == null) {
            return "Undefined message";
        }
        return json;
    }
}
