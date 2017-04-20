package com.stubbornjava.common.undertow;
import io.undertow.server.HttpServerExchange;
import okhttp3.HttpUrl;

public interface Urls {

    // {{start:currentUrl}}
    default HttpUrl currentUrl(HttpServerExchange exchange) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(exchange.getRequestURL()).newBuilder();

        if (!"".equals(exchange.getQueryString())) {
            urlBuilder = urlBuilder.encodedQuery(exchange.getQueryString());
        }
        return urlBuilder.build();
    }
    // {{end:currentUrl}}
}
