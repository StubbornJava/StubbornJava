package com.stubbornjava.examples.undertow.helloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HelloWorldServer {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServer.class);

    /*
     * Creating HttpHandlers as a method and passing it as a method reference is pretty clean.
     * This also helps reduce accidentally adding state to handlers.
     */
    public static void helloWorldHandler(HttpServerExchange exchange) {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World!");
    }

    public static void main(String[] args) {
        int port = 8080;
        /*
         *  "localhost" will ONLY listen on local host.
         *  If you want the server reachable from the outside you need to set "0.0.0.0"
         */
        String host = "localhost";

        /*
         * If you really want to abstract out the server construction you can.
         * StubbornJava recommends to only use abstractions / wrappers on very simple APIs.
         * When you abstract over something like an HTTP server its very difficult to have
         * an abstraction that covers the nuances of each webserver. Instead accept it and let
         * the framework bleed out a bit. Realistically you shouldn't be changing your web server
         * all that often. Even if you did fully abstract it out your custom logic tends to bleed
         * out eventually, embrace it and move on. You could also just have a static method or
         * factory that created the server with your common configurations and allows further
         * customization later.
         *
         * This web server has a single handler with no routing. All urls will respond the same way.
         */
        Undertow server = Undertow.builder()
            // Add the helloWorldHandler as a method reference.
            .addHttpListener(port, host, HelloWorldServer::helloWorldHandler)
            .build();
        logger.debug("starting on http://" + host + ":" + port);
        server.start();
    }
}
