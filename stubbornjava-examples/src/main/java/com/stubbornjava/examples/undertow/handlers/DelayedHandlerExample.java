package com.stubbornjava.examples.undertow.handlers;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Uninterruptibles;
import com.stubbornjava.common.Http;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.Timers;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.common.undertow.handlers.diagnostic.DelayedExecutionHandler;
import com.stubbornjava.common.undertow.handlers.diagnostic.DiagnosticHandlers;
import com.stubbornjava.examples.undertow.routing.RoutingHandlers;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import okhttp3.OkHttpClient;

public class DelayedHandlerExample {
    private static final Logger log = LoggerFactory.getLogger(DelayedHandlerExample.class);

    // {{start:router}}
    private static HttpHandler getRouter() {

        // Handler using Thread.sleep for a blocking delay
        HttpHandler sleepHandler = (exchange) -> {
            log.debug("In sleep handler");
            Uninterruptibles.sleepUninterruptibly(1L, TimeUnit.SECONDS);
            Exchange.body().sendText(exchange, "ok");
        };

        // Custom handler using XnioExecutor.executeAfter
        // internals for a non blocking delay
        DelayedExecutionHandler delayedHandler = DiagnosticHandlers.fixedDelay(
            (exchange) -> {
                log.debug("In delayed handler");
                Exchange.body().sendText(exchange, "ok");
            },
            1L, TimeUnit.SECONDS);

        HttpHandler routes = new RoutingHandler()
            .get("/sleep", sleepHandler)
            .get("/dispatch/sleep", new BlockingHandler(sleepHandler))
            .get("/delay", delayedHandler)
            .get("/dispatch/delay", new BlockingHandler(delayedHandler))
            .setFallbackHandler(RoutingHandlers::notFoundHandler);

        return CustomHandlers.accessLog(routes, LoggerFactory.getLogger("Access Log"));
    }
    // {{end:router}}

    // {{start:main}}
    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(getRouter());
        server.getUndertow()
              .setIoThreads(1)
              .setWorkerThreads(5);
        Undertow undertow = server.start();

        OkHttpClient client = HttpClient.globalClient();

        Timers.time("---------- sleep ----------", () ->
            Http.getInParallel(client, "http://localhost:8080/sleep", 5));

        Timers.time("---------- dispatch sleep ----------", () ->
            Http.getInParallel(client, "http://localhost:8080/dispatch/sleep", 5));

        Timers.time("---------- delay ----------", () ->
            Http.getInParallel(client, "http://localhost:8080/delay", 5));

        Timers.time("---------- dispatch delay ----------", () ->
            Http.getInParallel(client, "http://localhost:8080/dispatch/delay", 5));
        undertow.stop();
    }
    // {{end:main}}
}
