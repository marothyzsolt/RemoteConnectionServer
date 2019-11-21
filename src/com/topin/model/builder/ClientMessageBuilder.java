package com.topin.model.builder;

import com.topin.actions.ActionBase;
import com.topin.actions.ActionLock;
import com.topin.model.command.CommandMessage;
import com.topin.model.contracts.MessageContract;

public class ClientMessageBuilder {

    private ActionBase action;
    private String targetToken;
    private String fromToken;

    public ClientMessageBuilder(String commandType, String command) {
        switch (commandType) {
            case "lock": this.action = new ActionLock(); break;
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
