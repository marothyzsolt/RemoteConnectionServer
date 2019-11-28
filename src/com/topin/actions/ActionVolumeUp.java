package com.topin.actions;

public class ActionVolumeUp extends ActionBase {
    @Override
    public String command() {
        return "cmd.exe /c PowerShell -Command \"(New-Object -ComObject WScript.Shell).SendKeys([char]175)\"";
    }
}
