package com.stubbornjava.examples.okhttp;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.HttpClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpLogging {
    private static final Logger log = LoggerFactory.getLogger(OkHttpLogging.class);

    // {{start:request}}
    private static void request(OkHttpClient client, String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Unchecked.supplier(() -> {
            Response response = client.newCall(request).execute();
            log.debug("{} - {}", response.code(), response.body().string());
            return null;
        }).get();
    }
    // {{end:request}}

    public static void main(String[] args) {
        // {{start:noLogging}}
        log.debug("noLogging");
        OkHttpClient client = new OkHttpClient.Builder().build();
        request(client, "http://localhost:8080/redirectToPing");
        // {{end:noLogging}}

        // {{start:interceptor}}
        log.debug("interceptor");
        OkHttpClient interceptorClient = new OkHttpClient.Builder()
            .addInterceptor(HttpClient.getLoggingInterceptor())
            .build();
        request(interceptorClient, "http://localhost:8080/redirectToPing");
        // {{end:interceptor}}


        // {{start:networkInterceptor}}
        log.debug("networkInterceptor");
        OkHttpClient networkInterceptorClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(HttpClient.getLoggingInterceptor())
            .build();
        request(networkInterceptorClient, "http://localhost:8080/redirectToPing");
        // {{end:networkInterceptor}}
    }
}
