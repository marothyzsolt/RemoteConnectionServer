package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class ScreenshotMessage extends Message {
    private boolean status;
    private String screenshot;

    public ScreenshotMessage(boolean status, String screenshot) {
        super("screenshot");
        this.status = status;
        this.screenshot = screenshot;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "screenshot")
                .put("success", this.status)
                .put("screenshot", this.screenshot)
                .toString();
    }
}
