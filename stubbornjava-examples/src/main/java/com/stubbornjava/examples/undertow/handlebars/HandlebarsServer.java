package com.stubbornjava.examples.undertow.handlebars;

import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class HandlebarsServer {

    // {{start:server}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/messageRawTemplate", HandlebarsHandlers::messageRawTemplate)
        .get("/messagesRawTemplate", HandlebarsHandlers::messagesRawTemplate)
        .get("/messagesTemplate", HandlebarsHandlers::messagesTemplate)
    ;

    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        server.start();
    }
    // {{end:server}}
}
