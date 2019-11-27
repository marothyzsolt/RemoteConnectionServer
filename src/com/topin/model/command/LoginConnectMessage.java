package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class LoginConnectMessage extends Message {
    private String username;
    private String password;

    public LoginConnectMessage(String username, String password) {
        super("loginConnect");
        this.username = username;
        this.password = password;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "loginConnect")
                .put("username", this.username)
                .put("password", this.password)
                .toString();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
