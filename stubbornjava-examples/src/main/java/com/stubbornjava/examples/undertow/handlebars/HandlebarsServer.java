package com.stubbornjava.examples.undertow.handlebars;

import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class HandlebarsServer {

    // {{start:routes}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/messageRawTemplate", HandlebarsHandlers::messageRawTemplate)
        .get("/messagesRawTemplate", HandlebarsHandlers::messagesRawTemplate)
        .get("/messagesTemplate", HandlebarsHandlers::messagesTemplate)
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        server.start();
    }
    // {{end:server}}
}
