package com.stubbornjava.common;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

public class HttpClient {
    private HttpClient() {
    };

    // {{start:logging}}
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static final HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor((msg) -> {
            logger.debug(msg);
        });
    static {
        loggingInterceptor.setLevel(Level.BODY);
    }

    public static HttpLoggingInterceptor getLoggingInterceptor() {
        return loggingInterceptor;
    }
    // {{end:logging}}

    // {{start:client}}
    private static final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addNetworkInterceptor(loggingInterceptor)
        .build()
    ;

    /*
     * Global client that can be shared for common HTTP tasks.
     */
    public static OkHttpClient globalClient() {
        return client;
    }
    // {{end:client}}

    // {{start:cookieJar}}
    /*
     * Creates a new client from the global client with
     * a stateful cookie jar. This is useful when you need
     * to access password protected sites.
     */
    public static OkHttpClient newClientWithCookieJar() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        return client.newBuilder().cookieJar(cookieJar).build();
    }
    // {{end:cookieJar}}
}
