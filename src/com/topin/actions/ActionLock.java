package com.topin.actions;

public class ActionLock extends ActionBase {
    @Override
    public String command() {
        return "cmd.exe /c rundll32.exe user32.dll,LockWorkStation";
    }
}
