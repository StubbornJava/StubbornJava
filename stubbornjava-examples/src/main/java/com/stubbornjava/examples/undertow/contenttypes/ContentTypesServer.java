package com.stubbornjava.examples.undertow.contenttypes;

import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.server.RoutingHandler;

public class ContentTypesServer {

    // {{start:routes}}
    /*
     * Pretty clean am I right? Unless you REALLY REALLY love annotations
     * and the magic that comes with them. Don't forget about the copious
     * rabbit holes just trying to figure out what code executes because of
     * the annotation. This approach all code is just a few clicks away in
     * your IDE of choice. Your time is better spent writing code than pretending
     * to be a CSI cyber crimes specialist ripping apart some code for a clue to
     * whatever it does.
     */
    public static final RoutingHandler ROUTES = new RoutingHandler()
        .get("/helloWorldText", ContentTypeHandlers::helloWorldText)
        .get("/helloWorldHtml", ContentTypeHandlers::helloWorldHtml)
        .get("/helloWorldFileDownload", ContentTypeHandlers::helloWorldFileDownload)
        .get("/helloWorldJson", ContentTypeHandlers::helloWorldJson)
        .get("/helloWorldJsonSender", ContentTypeHandlers::helloWorldJsonSender)
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        server.start();
    }
    // {{end:server}}
}
