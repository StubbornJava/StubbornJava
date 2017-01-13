package com.stubbornjava.undertow.handlers;


import java.util.function.Function;

import io.undertow.server.HttpHandler;

public class MiddlewareBuilder {
    private final Function<HttpHandler, HttpHandler> function;

    private MiddlewareBuilder(Function<HttpHandler, HttpHandler> function) {
        if (null == function) {
            throw new IllegalArgumentException("Middleware Function can not be null");
        }
        this.function = function;
    }

    public static MiddlewareBuilder begin(Function<HttpHandler, HttpHandler> function) {
        return new MiddlewareBuilder(function);
    }

    public MiddlewareBuilder next(Function<HttpHandler, HttpHandler> before) {
        return new MiddlewareBuilder(function.compose(before));
    }

    public HttpHandler complete(HttpHandler handler) {
        return function.apply(handler);
    }
}
