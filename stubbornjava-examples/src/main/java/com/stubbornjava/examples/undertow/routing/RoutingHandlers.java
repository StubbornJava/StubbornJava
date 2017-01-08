package com.stubbornjava.examples.undertow.routing;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RoutingHandlers {

    // {{start:handler}}
    /*
     * Creating static factory methods to construct handlers let's you keep
     * them better organized and reduce some boilerplate. This will be shown
     * in future examples.
     */
    public static HttpHandler constantStringHandler(String value) {
        return new ConstantStringHandler(value);
    }
    // {{end:handler}}

    // {{start:anonymous}}
    /*
     * Alternate way to create constantStringHandler using an anonymous HttpHandler.
     * This is fine for simple handlers but more complex ones might be better off
     * in their own file.
     */
    public static HttpHandler constantStringHandlerAlt(String value) {
        return (HttpServerExchange exchange) -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(value);
        };
    }
    // {{end:anonymous}}

    // {{start:method}}
    /*
     * This is a 3rd approach to creating HttpHandlers and is heavily utilized
     * in StubbornJava. We can use Java 8 method references for this approach.
     * Notice how the void return type and single HttpServerExchange parameter
     * fulfill the HttpHandler interface.
     *
     * This approach is most commonly used for the actual business logic and generally
     * is responsible for sending the response. Any handlers that chain / delegate
     * to other handlers are not a great fit for this style.
     */
    public static void notFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Page Not Found!!");
    }
    // {{end:method}}
}
