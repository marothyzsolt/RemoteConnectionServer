package com.topin.model.builder;

import com.topin.helpers.JsonHelper;
import com.topin.model.Message;
import com.topin.model.command.*;
import com.topin.model.contracts.MessageContract;
import com.topin.services.Log;
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
                case "noTargetServer":
                    messageBuilder = new NoTargetServerMessage();
                    break;
                case "mouseMove":
                    messageBuilder = new MouseMoveMessage((Integer) this.data.get("x"), (Integer) this.data.get("y"));
                    if (this.data.containsKey("from")) { // TODO: REFACTOR
                        ((MouseMoveMessage) messageBuilder).setFromToken((String) this.data.get("from"));
                    }
                    if (this.data.containsKey("target")) {
                        ((MouseMoveMessage) messageBuilder).setTargetToken((String) this.data.get("target"));
                    }
                    break;
                case "mouseClick":
                    messageBuilder = new MouseClickMessage((String) this.data.get("button"), (Integer) this.data.get("mouseType"));
                    if (this.data.containsKey("from")) { // TODO: REFACTOR
                        ((MouseClickMessage) messageBuilder).setFromToken((String) this.data.get("from"));
                    }
                    if (this.data.containsKey("target")) {
                        ((MouseClickMessage) messageBuilder).setTargetToken((String) this.data.get("target"));
                    }
                    break;
                case "screenshot": {
                    messageBuilder = new ScreenMessage((String) this.data.get("imageBase64"));
                    ((ScreenMessage) messageBuilder).setFromToken((String) this.data.get("from"));
                    ((ScreenMessage) messageBuilder).setTargetToken((String) this.data.get("target"));
                    break;
                }
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
                case "request":
                    messageBuilder = new RequestMessage((String) this.data.get("request"), (String) this.data.get("parameter"));
                    ((RequestMessage) messageBuilder).setFromToken((String) this.data.get("from"));
                    ((RequestMessage) messageBuilder).setTargetToken((String) this.data.get("target"));
                    break;
                case "init":
                    String cpuUsage = (this.data.get("cpuUsage")) + "";
                    if (! cpuUsage.contains(".")) {
                        cpuUsage += ".0";
                    }
                    Double cpuUsageDouble = Double.valueOf(cpuUsage);

                    messageBuilder = new InitMessage(
                            (String) this.data.get("hostname"),
                            (String) this.data.get("cpuName"),
                            (String) this.data.get("localIp"),
                            (String) this.data.get("osName"),
                            (String) this.data.get("osVersion"),
                            (String) this.data.get("biosVersion"),
                            cpuUsageDouble,
                            (String) this.data.get("ramMax"),
                            (String) this.data.get("ramUsage"),
                            (String) this.data.get("driveUsage"),
                            (String) this.data.get("taskList"),
                            (String) this.data.get("backgroundImage")
                    );

                    if (this.data.containsKey("from")) { // TODO: REFACTOR
                        ((InitMessage) messageBuilder).setFromToken((String) this.data.get("from"));
                    }
                    if (this.data.containsKey("target")) {
                        ((InitMessage) messageBuilder).setTargetToken((String) this.data.get("target"));
                    }
                    break;
                case "clientCommand":
                    ClientMessageBuilder clientMessageBuilder =
                            new ClientMessageBuilder((String) this.data.get("commandType"), (String) this.data.get("command"));
                    clientMessageBuilder.setFromToken((String) this.data.get("from"));
                    clientMessageBuilder.setTargetToken((String) this.data.get("target"));

                    messageBuilder = clientMessageBuilder.makeSendableCommand();
                    break;
                default:
                    Log.write(this).error("Got undefined typed message: " + this.data.toString());
                    messageBuilder = new UndefinedMessage("undefined");
                    break;
            }
            return messageBuilder;
        } catch (NullPointerException e) {
            Log.write(this).error("Error in json string (not syntax error). Called undefined parameter inside json object. Type: " + this.type + "; Json: " + this.data.toString());
        } catch (ClassNotFoundException e) {
            Log.write(this).error("Maybe clientCommand type not found, please modify ClientMessageBuilder class: " + e.getMessage());
        }
        return new UndefinedMessage("undefined");
    }

    public static Message build(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (! jsonObject.has("type")) {
                Log.write("MessageBuilder").error("The JSON do not have 'type' parameter: " + jsonData);
            }
            MessageBuilder messageBuilder = new MessageBuilder((String) jsonObject.get("type"));
            messageBuilder.setData(JsonHelper.jsonObjectToHashMap(jsonObject));
            Message messageObject = (Message) messageBuilder.get();
            return messageObject;
        } catch (JSONException err){
            Log.write("MessageBuilder").error("Json exception on message: " + err.getMessage() + ". " + jsonData);
        } catch (Exception e) {
            Log.write("MessageBuilder").error("ERROR with jsonData: " + jsonData);
            e.printStackTrace();
        }
        return null;
    }


}
