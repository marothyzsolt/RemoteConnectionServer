package com.topin.actions;

public class ActionShutdownCancel extends ActionBase {
    @Override
    public String command() {
        return "shutdown /a";
    }
}
