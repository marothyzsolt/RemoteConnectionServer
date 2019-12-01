package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class ScreenMessage extends Message {
    private String imageBase64;

    public ScreenMessage(String imageBase64) {
        super("screenshot");
        this.imageBase64 = imageBase64;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "screenshot")
                .put("imageBase64", this.imageBase64)
                .toString();
    }

    @Override
    public String toOutput() {
        return new JSONObject()
                .put("type", "screenshot")
                .put("imageBase64", "Size: "+this.imageBase64.length())
                .toString();
    }
}
