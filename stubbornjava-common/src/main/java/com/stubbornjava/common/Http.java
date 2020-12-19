package com.stubbornjava.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.MoreExecutors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http {
    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(Http.class);

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

    // {{start:getInParallel}}
    public static void getInParallel(OkHttpClient client, String url, int count) {
        ExecutorService exec = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            exec.submit(() -> Http.get(client, url));
        }
        MoreExecutors.shutdownAndAwaitTermination(exec, 30, TimeUnit.SECONDS);
    }
    // {{end:getInParallel}}
}
