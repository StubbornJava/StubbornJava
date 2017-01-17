package com.stubbornjava.undertow.exchange;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public interface RedirectSenders {

    /*
     * Temporary redirect
     */
    default void temporary(HttpServerExchange exchange, String location) {
        exchange.setStatusCode(StatusCodes.FOUND);
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        exchange.endExchange();
    }

    /*
     * Permanent redirect
     */
    default void permanent(HttpServerExchange exchange, String location) {
        exchange.setStatusCode(StatusCodes.MOVED_PERMANENTLY);
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        exchange.endExchange();
    }

    /*
     * Temporary Redirect to the previous page based on the Referrer header.
     * This is very useful when you want to redirect to the previous
     * page after a form submission.
     */
    default void referer(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.FOUND);
        exchange.getResponseHeaders().put(Headers.LOCATION, exchange.getRequestHeaders().get(Headers.REFERER, 0));
        exchange.endExchange();
    }
}
