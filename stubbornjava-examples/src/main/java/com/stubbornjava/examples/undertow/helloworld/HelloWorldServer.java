package com.stubbornjava.examples.undertow.helloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HelloWorldServer {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServer.class);

    // {{start:helloworld}}
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
         * This web server has a single handler with no routing.
         * ALL urls are handled by the helloWorldHandler.
         */
        Undertow server = Undertow.builder()
            // Add the helloWorldHandler as a method reference.
            .addHttpListener(port, host, HelloWorldServer::helloWorldHandler)
            .build();
        logger.debug("starting on http://" + host + ":" + port);
        server.start();
    }
    // {{end:helloworld}}
}
