package com.topin.model.builder;

import com.topin.helpers.JsonHelper;
import com.topin.model.Message;
import com.topin.model.command.*;
import com.topin.model.contracts.MessageContract;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageBuilder extends BuilderBase {
    public MessageBuilder(String type) {
        super(type);
    }


    public MessageContract get() {
        MessageContract messageBuilder;
        try {
            switch (this.type) {
                case "login":
                    messageBuilder = new LoginMessage((String) this.data.get("clientType"), (String) this.data.get("token"));
                    break;
                case "loginConnect":
                    messageBuilder = new LoginConnectMessage((String) this.data.get("username"), (String) this.data.get("password"));
                    break;
                case "status":
                    messageBuilder = new StatusMessage((Boolean) this.data.get("success"), (String) this.data.get("message"));
                    break;
                case "command":
                    messageBuilder = new CommandMessage((String) this.data.get("command"));
                    break;
                case "init":
                    messageBuilder = new InitMessage(
                            (String) this.data.get("hostname"),
                            (String) this.data.get("cpuName"),
                            (String) this.data.get("localIp"),
                            (String) this.data.get("osName"),
                            (String) this.data.get("osVersion"),
                            (String) this.data.get("biosVersion"),
                            (Double) this.data.get("cpuUsage"),
                            (Integer) this.data.get("ramMax"),
                            (Integer) this.data.get("ramUsage"),
                            (String) this.data.get("driveUsage"),
                            (String) this.data.get("taskList")
                    );
                    break;
                case "clientCommand":
                    ClientMessageBuilder clientMessageBuilder =
                            new ClientMessageBuilder((String) this.data.get("commandType"), (String) this.data.get("command"));
                    clientMessageBuilder.setFromToken((String) this.data.get("from"));
                    clientMessageBuilder.setTargetToken((String) this.data.get("target"));

                    messageBuilder = clientMessageBuilder.makeSendableCommand();
                    break;
                default:
                    System.out.println("Got undefined typed message: " + this.data.toString());
                    messageBuilder = new UndefinedMessage("undefined");
                    break;
            }
            return messageBuilder;
        } catch (NullPointerException e) {
            System.out.println("Error in json string (not syntax error). Called undefined parameter inside json object.");
        }
        return new UndefinedMessage("undefined");
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
            System.out.println("Json exception on message: " + jsonData);
        }
        return null;
    }


}
