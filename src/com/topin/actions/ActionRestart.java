package com.topin.actions;

public class ActionRestart extends ActionBase {
    @Override
    public String command() {
        return "shutdown /r /t 45";
    }
}
