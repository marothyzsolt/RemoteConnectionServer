package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class KeyCodeMessage extends Message {
    private boolean status;
    private String keycode;
    private Integer pushType;

    public KeyCodeMessage(String keycode, Integer pushType) {
        super("request");
        this.keycode = keycode;
        this.pushType = pushType;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "request")
                .put("keycode", this.keycode)
                .put("pushType", this.pushType)
                .put("from", this.getFromToken())
                .put("target", this.getTargetToken())
                .toString();
    }
}
