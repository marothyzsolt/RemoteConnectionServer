package com.topin.model.builder;

import com.topin.actions.*;
import com.topin.model.command.CommandMessage;
import com.topin.model.contracts.MessageContract;

public class ClientMessageBuilder {

    private ActionBase action;
    private String targetToken;
    private String fromToken;

    public ClientMessageBuilder(String commandType, String command) throws ClassNotFoundException {
        switch (commandType) {
            case "lock": this.action = new ActionLock(); break;
            case "shutdown": this.action = new ActionShutdown(); break;
            case "customCommand": this.action = new ActionCustom(command); break;
            case "alert": this.action = new ActionAlert(command); break;
            default: throw new ClassNotFoundException("The commandType '"+ commandType +"' not found.");
        }
    }

    public MessageContract makeSendableCommand() {
        CommandMessage message = new CommandMessage(this.action.command());
        message.setFromToken(this.fromToken);
        message.setTargetToken(this.targetToken);
        return message;
    }

    public void setTargetToken(String targetToken) {
        this.targetToken = targetToken;
    }
    public void setFromToken(String fromToken) {
        this.fromToken = fromToken;
    }
}
