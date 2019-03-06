package com.stubbornjava.common.undertow.handlers.diagnostic;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.undertow.server.Connectors;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;

// {{start:delayedHandler}}
/**
 * A non blocking handler to add a time delay before the next handler
 * is executed. If the exchange has already been dispatched this will
 * un-dispatch the exchange and re-dispatch it before next is called.
 */
public class DelayedExecutionHandler implements HttpHandler {

    private final HttpHandler next;
    private final Function<HttpServerExchange, Duration> durationFunc;

    DelayedExecutionHandler(HttpHandler next,
                            Function<HttpServerExchange, Duration> durationFunc) {
        this.next = next;
        this.durationFunc = durationFunc;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Duration duration = durationFunc.apply(exchange);

        final HttpHandler delegate;
        if (exchange.isBlocking()) {
            // We want to undispatch here so that we are not blocking
            // a worker thread. We will spin on the IO thread using the
            // built in executeAfter.
            exchange.unDispatch();
            delegate = new BlockingHandler(next);
        } else {
            delegate = next;
        }

        exchange.dispatch(exchange.getIoThread(), () -> {
            exchange.getIoThread().executeAfter(() ->
                Connectors.executeRootHandler(delegate, exchange),
                duration.toMillis(),
                TimeUnit.MILLISECONDS);
        });
    }
}
// {{end:delayedHandler}}
