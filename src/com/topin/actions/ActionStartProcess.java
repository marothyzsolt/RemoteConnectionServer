package com.topin.actions;

public class ActionStartProcess extends ActionBase {
    private String parameter;

    public ActionStartProcess(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String command() {
        return "cmd.exe /c " + this.parameter;
    }
}
