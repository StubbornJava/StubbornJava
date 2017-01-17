package com.stubbornjava.examples.undertow.redirects;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class RedirectServer {

    // {{start:redirects}}
    public static void hello(HttpServerExchange exchange) {
        Exchange.body().sendText(exchange, "Hello");
    }

    public static void temporaryRedirect(HttpServerExchange exchange) {
        Exchange.redirect().temporary(exchange, "/hello");
    }

    public static void permanentRedirect(HttpServerExchange exchange) {
        Exchange.redirect().permanent(exchange, "/hello");
    }

    public static void referrerRedirect(HttpServerExchange exchange) {
        Exchange.redirect().referer(exchange);
    }

    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/hello", RedirectServer::hello)
        .get("/temporaryRedirect", RedirectServer::temporaryRedirect)
        .get("/permanentRedirect", RedirectServer::permanentRedirect)
        .get("/referrerRedirect", RedirectServer::referrerRedirect)
    ;

    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        server.start();
    }
    // {{end:redirects}}
}
