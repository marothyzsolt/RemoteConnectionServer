package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class StatusMessage extends Message {
    private boolean status;

    public StatusMessage(boolean status) {
        super("status");
        this.status = status;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("success", this.status)
                .toString();
    }
}
