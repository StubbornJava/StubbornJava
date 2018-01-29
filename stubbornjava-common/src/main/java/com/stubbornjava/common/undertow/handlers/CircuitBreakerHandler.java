package com.stubbornjava.common.undertow.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;

// {{start:handler}}
public class CircuitBreakerHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerHandler.class);

    private final CircuitBreaker circuitBreaker;
    private final HttpHandler delegate;
    private final HttpHandler failureHandler;

    public CircuitBreakerHandler(CircuitBreaker circuitBreaker, HttpHandler delegate, HttpHandler failureHandler) {
        super();
        this.circuitBreaker = circuitBreaker;
        this.delegate = delegate;
        this.failureHandler = failureHandler;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Failsafe.with(circuitBreaker)
                .withFallback(() -> failureHandler.handleRequest(exchange))
                // We need to call get here instead of execute so we can return the
                // mutated exchange to run checks on it
                .get(() -> {
                    delegate.handleRequest(exchange);
                    return exchange;
                });
    }
}
// {{end:handler}}

