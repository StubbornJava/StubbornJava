package com.stubbornjava.examples.undertow.accesslog;

import org.slf4j.LoggerFactory;

import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.examples.undertow.routing.RoutingHandlers;
import com.stubbornjava.undertow.handlers.accesslog.Slf4jAccessLogReceiver;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;

public class AccessLogServer {

    // {{start:routes}}
    // For brevity just borrow the RoutingServer routes. Copy Pasta!
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/", RoutingHandlers.constantStringHandler("GET - My Homepage"))
        .get("/myRoute", RoutingHandlers.constantStringHandler("GET - My Route"))
        .post("/myRoute", RoutingHandlers.constantStringHandler("POST - My Route"))
        .get("/myOtherRoute", RoutingHandlers.constantStringHandler("GET - My Other Route"))
        // Wildcards and RoutingHandler had some bugs before version 1.4.8.Final
        .get("/myRoutePrefix*", RoutingHandlers.constantStringHandler("GET - My Prefixed Route"))
        // Pass a handler as a method reference.
        .setFallbackHandler(RoutingHandlers::notFoundHandler)
    ;
    // {{end:routes}}

    // {{start:accesslog}}
    private static final HttpHandler ROOT = new AccessLogHandler(
        ROUTES,
        new Slf4jAccessLogReceiver(LoggerFactory.getLogger("com.stubbornjava.accesslog")),
        "combined",
        AccessLogServer.class.getClassLoader())
    ;
    // {{end:accesslog}}

    // {{start:server}}
    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(ROOT);
        server.start();
    }
    // {{end:server}}
}
