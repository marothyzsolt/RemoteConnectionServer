package com.topin.model.builder;

import com.topin.actions.ActionBase;
import com.topin.actions.ActionLock;
import com.topin.model.Message;
import com.topin.model.command.CommandMessage;
import com.topin.model.contracts.MessageContract;

public class ClientMessageBuilder {

    private ActionBase action;

    public ClientMessageBuilder(String commandType, String command) {
        switch (commandType) {
            case "lock": this.action = new ActionLock(); break;
        }
    }

    public MessageContract makeSendableCommand() {
        return new CommandMessage(this.action.command());
    }
}
