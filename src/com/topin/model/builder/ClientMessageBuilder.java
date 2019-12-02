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
            case "shutdownCancel": this.action = new ActionShutdownCancel(); break;
            case "restart": this.action = new ActionRestart(); break;
            case "customCommand": this.action = new ActionCustom(command); break;
            case "alert": this.action = new ActionAlert(command); break;
            case "volumeControlUp": this.action = new ActionVolumeUp(); break;
            case "volumeControlDown": this.action = new ActionVolumeDown(); break;
            case "volumeControlMute": this.action = new ActionVolumeMute(); break;
            case "playSound": this.action = new ActionPlaySound(command.length() == 0 ? "c:\\WINDOWS\\Media\\notify.wav" : command); break;
            case "taskKill": this.action = new ActionTaskKill(command); break;
            case "openBrowser": this.action = new ActionOpenBrowser(); break;
            case "startProcess": this.action = new ActionStartProcess(command); break;
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
