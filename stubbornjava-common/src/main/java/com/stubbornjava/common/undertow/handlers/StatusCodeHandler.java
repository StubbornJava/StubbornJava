package com.stubbornjava.common.undertow.handlers;

import com.codahale.metrics.Meter;
import com.stubbornjava.common.Metrics;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/*
 * Depends on Metrics so it is here instead of in stubbornjava-undertow
 */
public class StatusCodeHandler implements HttpHandler {
    private final HttpHandler handler;
    private final String prefix;

    public StatusCodeHandler(HttpHandler handler, String prefix) {
        this.handler = handler;
        this.prefix = prefix;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        /**
         * Call the underlying handler first since it may be setting the status code.
         * Assume there are no exceptions.  We should catch and handle them before this.
         */
        handler.handleRequest(exchange);

        int code = exchange.getStatusCode();
        Meter meter = Metrics.meter(prefix, String.valueOf(code));
        meter.mark();
    }
}
