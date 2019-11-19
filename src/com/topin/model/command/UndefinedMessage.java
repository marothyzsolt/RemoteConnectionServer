package com.topin.model.command;

import com.topin.model.Message;

public class UndefinedMessage extends Message {
    public UndefinedMessage(String type) {
        super(type);
    }

    @Override
    public String toJson() {
        return null;
    }
}
