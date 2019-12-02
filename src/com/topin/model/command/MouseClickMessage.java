package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class MouseClickMessage extends Message {
    private String button;
    private Integer mouseType;

    public MouseClickMessage(String button, Integer mouseType) { super("mouseClick");
        this.button = button;
        this.mouseType = mouseType;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "mouseClick")
                .put("button", this.button)
                .put("mouseType", this.mouseType)
                .put("from", this.getFromToken())
                .put("target", this.getTargetToken())
                .toString();
    }
}
