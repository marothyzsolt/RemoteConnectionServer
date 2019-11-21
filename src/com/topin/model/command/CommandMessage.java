package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class CommandMessage extends Message {
    private String command;

    public CommandMessage(String command) {
        super("command");
        this.command = command;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "command")
                .put("command", this.command)
                .put("from", this.getFromToken())
                .put("target", this.getTargetToken())
                .toString();
    }
}
