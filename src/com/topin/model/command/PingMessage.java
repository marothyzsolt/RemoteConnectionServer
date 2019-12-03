package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class PingMessage extends Message {

    public PingMessage() {
        super("ping");
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "ping")
                .toString();
    }
}
