package com.topin.actions;

public class ActionTaskKill extends ActionBase {
    private String pid; // c:\WINDOWS\Media\notify.wav

    public ActionTaskKill(String pid) {
        this.pid = pid;
    }

    @Override
    public String command() {
        return "cmd.exe /c taskkill /F /IM " + this.pid;
    }
}
