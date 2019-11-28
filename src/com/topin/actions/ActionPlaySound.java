package com.topin.actions;

public class ActionPlaySound extends ActionBase {
    private String soundPath; // c:\WINDOWS\Media\notify.wav

    public ActionPlaySound(String soundPath) {
        this.soundPath = soundPath;
    }

    @Override
    public String command() {
        return "cmd.exe /c powershell -c echo `a";
    }
}
