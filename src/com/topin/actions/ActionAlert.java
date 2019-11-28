package com.topin.actions;

public class ActionAlert extends ActionBase {
    private String message;

    public ActionAlert(String message) {
        this.message = message;
    }

    @Override
    public String command() {
        return "cmd.exe /c msg \"%username%\" " + this.message;
    }
}
