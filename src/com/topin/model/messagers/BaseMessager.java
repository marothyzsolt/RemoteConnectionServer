package com.topin.model.messagers;

import com.topin.model.Message;
import com.topin.services.ClientConnection;

abstract public class BaseMessager {
    protected Message clientMessage = null;
    protected ClientConnection baseClientConnection = null;

    public BaseMessager(Message clientMessage, ClientConnection baseClientConnection)
    {
        this.clientMessage = clientMessage;
        this.baseClientConnection = baseClientConnection;
    }

    public abstract void handle();
}
