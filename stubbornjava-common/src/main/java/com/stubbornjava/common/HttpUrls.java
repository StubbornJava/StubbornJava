package com.stubbornjava.common;

import java.util.List;

import okhttp3.HttpUrl;

public class HttpUrls {

    public static HttpUrl host(HttpUrl url) {
        List<String> pathSegments = url.pathSegments();
        HttpUrl.Builder urlBuilder = url.newBuilder();
        for (int i = pathSegments.size() - 1; i >= 0; i--) {
            urlBuilder.removePathSegment(i);
        }
        urlBuilder.query(null);
        return urlBuilder.build();
    }
}
