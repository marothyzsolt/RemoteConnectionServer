package com.topin.model.builder;

import java.util.HashMap;

abstract public class BuilderBase {
    protected String type;
    protected HashMap<String, Object> data = new HashMap<>();

    public BuilderBase(String type) {
        this.type = type;
    }

    public BuilderBase add(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
}
