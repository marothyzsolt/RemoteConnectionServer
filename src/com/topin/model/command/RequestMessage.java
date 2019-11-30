package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class RequestMessage extends Message {
    private boolean status;
    private String request;
    private String parameter; // json

    public RequestMessage(String request, String parameter) {
        super("request");
        this.request = request;
        this.parameter = parameter;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "request")
                .put("request", this.request)
                .put("parameter", this.parameter)
                .put("from", this.getFromToken())
                .put("target", this.getTargetToken())
                .toString();
    }
}
