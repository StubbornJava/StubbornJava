package com.stubbornjava.webapp.guide;

import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;

public class GuideParams {
    private GuideParams() {}

    public static String slug(HttpServerExchange exchange) {
        return Exchange.pathParams().pathParam(exchange, "slug").orElse(null);
    }
}
