package com.stubbornjava.examples.okhttp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.Http;

import okhttp3.OkHttpClient;

public class OkHttpTestUtil {
    private OkHttpTestUtil() {}
    private static final Logger log = LoggerFactory.getLogger(OkHttpTestUtil.class);

    // {{start:getInParallel}}
    public static void getInParallel(OkHttpClient client, String url, int count) {
        ExecutorService exec = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            exec.submit(() -> Http.get(client, url));
        }
        exec.shutdown();
        try {
            exec.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("error shutting down executor", e);
        }
    }
    // {{end:getInParallel}}
}
