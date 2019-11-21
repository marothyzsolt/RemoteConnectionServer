package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class LoginMessage extends Message {
    private String clientType;
    private String token;

    public LoginMessage(String clientType, String token) {
        super("login");
        this.clientType = clientType;
        this.token = token;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("clientType", this.clientType)
                .put("token", this.token)
                .toString();
    }

    public String getClientType() {
        return clientType;
    }

    public String getToken() {
        return token;
    }
}
