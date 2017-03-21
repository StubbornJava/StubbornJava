package com.stubbornjava.common;

import okhttp3.MediaType;
import okhttp3.RequestBody;

// {{start:requestBodies}}
public class RequestBodies {
    private RequestBodies() {}

    public static RequestBody xml(String data) {
        return RequestBody.create(MediaType.parse("text/xml"), data);
    }

    public static RequestBody form(String data) {
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), data);
    }

    public static RequestBody json(String data) {
        return RequestBody.create(MediaType.parse("application/json"), data);
    }

    public static RequestBody jsonObj(Object data) {
        String json = Json.serializer().toString(data);
        return RequestBody.create(MediaType.parse("application/json"), json);
    }
}
// {{end:requestBodies}}
