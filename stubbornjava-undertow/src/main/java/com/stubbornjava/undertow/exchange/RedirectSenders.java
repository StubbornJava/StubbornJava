package com.stubbornjava.undertow.exchange;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public interface RedirectSenders {

    /*
     * Temporary redirect
     */
    default void temporary(HttpServerExchange exchange, String location) {
        exchange.setStatusCode(302);
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        exchange.endExchange();
    }

    /*
     * Permanent redirect
     */
    default void permanent(HttpServerExchange exchange, String location) {
        exchange.setStatusCode(301);
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        exchange.endExchange();
    }

    /*
     * Temporary Redirect to the previous page based on the Referrer header.
     * This is very useful when you want to redirect to the previous
     * page after a form submission.
     */
    default void referer(HttpServerExchange exchange) {
        exchange.setStatusCode(302);
        exchange.getResponseHeaders().put(Headers.LOCATION, exchange.getRequestHeaders().get(Headers.REFERER, 0));
        exchange.endExchange();
    }
}
