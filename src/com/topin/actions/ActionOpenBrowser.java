package com.topin.actions;

public class ActionOpenBrowser extends ActionBase {
    @Override
    public String command() {
        return "cmd.exe /c explorer \"https://google.com\"";
    }
}
