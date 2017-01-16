package com.stubbornjava.common.undertow.handlers;

import com.stubbornjava.undertow.handlers.MiddlewareBuilder;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;

public class Middleware {

    public static HttpHandler common(HttpHandler root) {
        return MiddlewareBuilder.begin(BlockingHandler::new)
                                .next(CustomHandlers::gzip)
                                .next(CustomHandlers::accessLog)
                                .next(CustomHandlers::statusCodeMetrics)
                                .complete(root);
    }
}
