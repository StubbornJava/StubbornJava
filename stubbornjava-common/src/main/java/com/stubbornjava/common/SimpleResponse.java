package com.stubbornjava.common;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.collect.Maps;

public class SimpleResponse {
    private Map<String, Object> data;

    SimpleResponse() {
        super();
        this.data = null;
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    public SimpleResponse with(String key, Object value) {
        if (null == data) {
            data = Maps.newLinkedHashMap();
        }
        if (data.containsKey(key)) {
            throw new IllegalArgumentException(key + " already has data");
        }
        data.put(key, value);
        return this;
    }

    public static SimpleResponse create() {
        return new SimpleResponse();
    }
}