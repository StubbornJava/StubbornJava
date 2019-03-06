package com.stubbornjava.common.undertow.handlers.diagnostic;

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
        Duration dau = durationFunc.apply(exchange);

        boolean wasDispatched = exchange.isBlocking();
        final HttpHandler delegate;
        if (wasDispatched) {
            exchange.unDispatch();
            delegate = new BlockingHandler(next);
        } else {
            delegate = next;
        }

        exchange.dispatch(exchange.getIoThread(), () -> {
            exchange.getIoThread().executeAfter(() ->
                Connectors.executeRootHandler(delegate, exchange),
                                              dau.getDuration(),
                                              dau.getUnit());
        });
    }

    static class Duration {
        private final long duration;
        private final TimeUnit unit;
        public Duration(long duration, TimeUnit unit) {
            super();
            this.duration = duration;
            this.unit = unit;
        }
        public long getDuration() {
            return duration;
        }
        public TimeUnit getUnit() {
            return unit;
        }
    }
}
// {{end:delayedHandler}}
