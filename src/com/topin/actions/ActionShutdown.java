package com.topin.actions;

public class ActionShutdown extends ActionBase {
    @Override
    public String command() {
        return "shutdown /r /t 180";
    }
}
