package com.stubbornjava.common.undertow.handlers;

import com.stubbornjava.undertow.handlers.MiddlewareBuilder;
import com.stubbornjava.undertow.handlers.ReferrerPolicyHandlers.ReferrerPolicy;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;

public class Middleware {

    public static HttpHandler common(HttpHandler root) {
        return MiddlewareBuilder.begin(handler -> CustomHandlers.securityHeaders(handler, ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                                .next(CustomHandlers::gzip)
                                .next(BlockingHandler::new)
                                .next(CustomHandlers::accessLog)
                                .next(CustomHandlers::statusCodeMetrics)
                                .complete(root);
    }
}
