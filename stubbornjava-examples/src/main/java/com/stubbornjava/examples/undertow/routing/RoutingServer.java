package com.stubbornjava.examples.undertow.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class RoutingServer {
    private static final Logger logger = LoggerFactory.getLogger(RoutingServer.class);

    // {{start:routing}}
    /*
     * Utilize Undertow's RoutingHandler for HTTP Verb / path routing.
     * We can utilize the fallback handler for the not found handler.
     *
     * Using the RoutingHandler we can generally achieve a single line per route.
     * It also decouples the HttpHander from the route which encourages reuse.
     * A good example would be a single error handler or not found handler that can
     * be passed to multiple routes or nodes in the routing tree as it gets more complex.
     * For instance you can have an ExceptionHandler that handles 404's by delegating to the
     * notFoundHandler and you can also use the same notFoundHandler as the RoutingHandlers
     * fallbackHandler.
     *
     * How you organize your routes is entirely up to you and Undertow is very flexible
     * in this regard. We tend to use a single routing file per service even if it gets
     * to 100+ routes. It is very convenient for searching code and trying to track down the
     * code that is executed for a given route. Also using the method reference style there
     * should be almost no logic in the file so readability isn't a major concern.
     *
     * If you do prefer splitting your routes out for reuse take a look at RoutingHandlers
     * addAll method. This allows you to pass a RoutingHandler into a RoutingHandler to add
     * all of its routes. Using this approach when you construct the server you can create a
     * new RoutingHandler and pass in each individual RoutingHandler. This can also be great
     * for common routes across services like health checks or system diagnostic routes.
     *
     * You might want to split the Routes into their own file separate from the server
     * initialization code.
     */
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
    // {{end:routing}}

    // {{start:server}}
    public static void main(String[] args) {
        /*
         * You caught us! We changed our mind and decided to add a small abstraction.
         * Notice however we have zero intention of hiding Undertow. We have embraced
         * Undertow as our web server and will allow it to be leaked out. This is a conscious
         * design decision to improve developer productivity.
         */
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        // See we have access to it here!
        Undertow.Builder undertow = server.getUndertow();
        server.start();
    }
    // {{end:server}}
}
