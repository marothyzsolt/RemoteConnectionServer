package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class MouseMoveMessage extends Message {
    private Integer x;
    private Integer y;

    public MouseMoveMessage(Integer x, Integer y) { super("mouseMove");
        this.x = x;
        this.y = y;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "mouseMove")
                .put("x", this.x)
                .put("y", this.y)
                .put("from", this.getFromToken())
                .put("target", this.getTargetToken())
                .toString();
    }
}
