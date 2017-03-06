package com.stubbornjava.examples.healthchecks;

import com.codahale.metrics.health.HealthCheck;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// {{start:health}}
public class ExternalServiceHealthCheck extends HealthCheck {
    private final OkHttpClient client;
    private final HttpUrl path;

    public ExternalServiceHealthCheck(OkHttpClient client, HttpUrl path) {
        this.client = client;
        this.path = path;
    }

    @Override
    protected Result check() throws Exception {
        Request request = new Request.Builder()
            .url(path)
            .get()
            .build();
        Response response = client.newCall(request).execute();

        // If response is a 20x response code pass it.
        if (response.isSuccessful()) {
            return Result.healthy();
        }
        return Result.unhealthy("code: %s - body: %s", response.code(), response.body().string());
    }
}
// {{end:health}}