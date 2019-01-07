package com.stubbornjava.common;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http {
    private static final Logger logger = LoggerFactory.getLogger(Http.class);

    // {{start:get}}
    public static Response get(OkHttpClient client, String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return Unchecked.supplier(() -> {
            Response response = client.newCall(request).execute();
            return response;
        }).get();
    }
    // {{end:get}}
}
