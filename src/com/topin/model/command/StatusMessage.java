package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class StatusMessage extends Message {
    private boolean status;
    private String message;

    public StatusMessage(boolean status, String message) {
        super("status");
        this.status = status;
        this.message = message;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "status")
                .put("success", this.status)
                .put("message", this.message)
                .toString();
    }
}
