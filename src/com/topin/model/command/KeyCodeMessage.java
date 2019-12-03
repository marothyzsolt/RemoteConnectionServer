package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class KeyCodeMessage extends Message {
    private Integer keyCode;

    public KeyCodeMessage(Integer keyCode) {
        super("keyCode");
        this.keyCode = keyCode;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "keyCode")
                .put("keyCode", this.keyCode)
                .put("from", this.getFromToken())
                .put("target", this.getTargetToken())
                .toString();
    }
}
