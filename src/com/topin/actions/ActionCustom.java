package com.topin.actions;

public class ActionCustom extends ActionBase {
    private String command = "";

    public ActionCustom(String command) {
        this.command = command;
    }

    @Override
    public String command() {
        return "cmd.exe /c " + command;
    }
}
