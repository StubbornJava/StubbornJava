package com.stubbornjava.undertow.handlers;

import java.util.function.Function;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.SetHeaderHandler;
import io.undertow.util.HttpString;

// {{start:handler}}
public class XFrameOptionsHandlers {
    private static final String X_FRAME_OPTIONS_STRING = "X-Frame-Options";
    private static final HttpString X_FRAME_OPTIONS = new HttpString(X_FRAME_OPTIONS_STRING);

    public static HttpHandler deny(HttpHandler next) {
        return new SetHeaderHandler(next, X_FRAME_OPTIONS_STRING, "DENY");
    }

    public static HttpHandler sameOrigin(HttpHandler next) {
        return new SetHeaderHandler(next, X_FRAME_OPTIONS_STRING, "SAMEORIGIN");
    }

    public static HttpHandler allowFromOrigin(HttpHandler next, String origin) {
        return new SetHeaderHandler(next, X_FRAME_OPTIONS_STRING, "ALLOW-FROM " + origin);
    }

    public static HttpHandler allowFromDynamicOrigin(HttpHandler next,
                                        Function<HttpServerExchange, String> originExtractor) {
        // Since this is dynamic skip using the SetHeaderHandler
        return exchange -> {
            exchange.getResponseHeaders().put(X_FRAME_OPTIONS, originExtractor.apply(exchange));
            next.handleRequest(exchange);
        };
    }
}
// {{end:handler}}