package com.topin.model.builder;

import com.topin.helpers.JsonHelper;
import com.topin.model.Message;
import com.topin.model.command.CommandMessage;
import com.topin.model.command.StatusMessage;
import com.topin.model.command.UndefinedMessage;
import com.topin.model.contracts.MessageContract;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageBuilder extends BuilderBase {
    public MessageBuilder(String type) {
        super(type);
    }


    public MessageContract get() {
        MessageContract messageBuilder;
        switch (this.type) {
            case "status":
                messageBuilder = new StatusMessage((Boolean) this.data.get("success"));
                break;
            case "command":
                messageBuilder = new CommandMessage((String) this.data.get("command"));
                break;
            default:
                messageBuilder = new UndefinedMessage("undefined");
                break;
        }
        return messageBuilder;
    }

    public static Message build(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (! jsonObject.has("type")) {
                System.out.println("The JSON do not have 'type' parameter: " + jsonData);
            }
            MessageBuilder messageBuilder = new MessageBuilder((String) jsonObject.get("type"));
            messageBuilder.setData(JsonHelper.jsonObjectToHashMap(jsonObject));
            Message messageObject = (Message) messageBuilder.get();
            return messageObject;
        } catch (JSONException err){
            System.out.println("Json exception on message");
        }
        return null;
    }


}
