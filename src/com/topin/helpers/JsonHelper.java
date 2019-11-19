package com.topin.helpers;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;

public class JsonHelper {
    public static HashMap jsonObjectToHashMap(JSONObject jsonObject) {
        try {
            return new ObjectMapper().readValue(jsonObject.toString(), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
