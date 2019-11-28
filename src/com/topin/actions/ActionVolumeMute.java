package com.topin.actions;

public class ActionVolumeMute extends ActionBase {
    @Override
    public String command() {
        return "cmd.exe /c PowerShell -Command \"(New-Object -ComObject WScript.Shell).SendKeys([char]173)\"";
    }
}
