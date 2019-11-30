package com.topin.model.messagers;

import com.topin.model.Message;
import com.topin.model.command.InitMessage;
import com.topin.services.ClientConnection;
import com.topin.services.Log;

public class MessagerInit extends BaseMessager{
    public MessagerInit(Message clientMessage, ClientConnection baseClientConnection) {
        super(clientMessage, baseClientConnection);
    }


    @Override
    public void handle() {
        Log.write(this).info("Store last init message from server: " + this.baseClientConnection.getCurrentClientToken());
        this.baseClientConnection.getCurrentClientData().setInitMessage((InitMessage) clientMessage);
    }

}
