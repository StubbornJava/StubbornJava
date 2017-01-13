package com.stubbornjava.common.undertow.handlers;

import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.stubbornjava.common.Metrics;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/*
 * Depends on Metrics so it is here instead of in stubbornjava-undertow
 */
public class TimingHttpHandler implements HttpHandler {
    private final HttpHandler handler;
    private final Timer timer;

    public TimingHttpHandler(HttpHandler handler, String name) {
        super();
        this.handler = handler;
        this.timer = Metrics.timer(name);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Context context = timer.time();
        try {
            handler.handleRequest(exchange);
        } finally {
            context.close();
        }
    }
}
