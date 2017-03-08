package com.stubbornjava.examples.okhttp;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.Middleware;
import com.stubbornjava.examples.undertow.routing.RoutingHandlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class OkHttpExampleServer {

    // {{start:routes}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/ping", timed("ping", (exchange) -> Exchange.body().sendText(exchange, "ok")))
        .get("/redirectToPing", (exchange) -> Exchange.redirect().temporary(exchange, "/ping"))
        .setFallbackHandler(timed("notFound", RoutingHandlers::notFoundHandler))
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
        // Once again pull in a bunch of common middleware.
        SimpleServer server = SimpleServer.simpleServer(Middleware.common(ROUTES));
        server.start();
    }
    // {{end:server}}
}
