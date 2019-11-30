package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class NoTargetServerMessage extends Message {

    public NoTargetServerMessage() {
        super("noTargetServer");
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "noTargetServer")
                .toString();
    }
}
