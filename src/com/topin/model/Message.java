package com.topin.model;

import com.topin.model.contracts.MessageContract;

abstract public class Message implements MessageContract {
    private String type;
    private String fromToken;
    private String targetToken;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromToken() {
        return fromToken;
    }

    public void setFromToken(String fromToken) {
        this.fromToken = fromToken;
    }

    public String getTargetToken() {
        return targetToken;
    }

    public void setTargetToken(String targetToken) {
        this.targetToken = targetToken;
    }
}
