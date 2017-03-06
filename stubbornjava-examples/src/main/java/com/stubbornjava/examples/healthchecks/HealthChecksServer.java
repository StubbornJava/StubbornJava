package com.stubbornjava.examples.healthchecks;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import com.codahale.metrics.health.HealthCheck;
import com.stubbornjava.common.HealthChecks;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.common.undertow.handlers.Middleware;
import com.stubbornjava.examples.hikaricp.ConnectionPools;
import com.stubbornjava.examples.undertow.routing.RoutingHandlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class HealthChecksServer {

    // {{start:routes}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/ping", timed("ping", (exchange) -> Exchange.body().sendText(exchange, "ok")))
        .get("/metrics", timed("metrics", CustomHandlers::metrics))
        .get("/health", timed("health", CustomHandlers::health))
        .setFallbackHandler(timed("notFound", RoutingHandlers::notFoundHandler))
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
        /*
         *  Init connection pools. They auto register their own health checks.
         */
        ConnectionPools.getProcessing();
        ConnectionPools.getTransactional();

        // Assume some global HttpClient.
        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl passingPath = HttpUrl.parse("http://localhost:8080/ping");
        HealthCheck passing = new ExternalServiceHealthCheck(client, passingPath);
        HealthChecks.getHealthCheckRegistry().register("ping", passing);

        // Since this route doesn't exist it will respond with 404 and should fail the check.
        HttpUrl failingPath = HttpUrl.parse("http://localhost:8080/failingPath");
        HealthCheck failing = new ExternalServiceHealthCheck(client, failingPath);
        HealthChecks.getHealthCheckRegistry().register("shouldFail", failing);

        // Once again pull in a bunch of common middleware.
        SimpleServer server = SimpleServer.simpleServer(Middleware.common(ROUTES));
        server.start();
    }
    // {{end:server}}
}
