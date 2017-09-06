package com.stubbornjava.examples.undertow;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.handlers.Middleware;
import com.stubbornjava.examples.undertow.contenttypes.ContentTypesServer;
import com.stubbornjava.examples.undertow.rest.RestServer;
import com.stubbornjava.examples.undertow.routing.ConstantStringHandler;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class MicroMonolith {

    public static void main(String[] args) {

        // {{start:restServer}}
        Undertow.builder()
                .addHttpListener(8080, "0.0.0.0", Middleware.common(RestServer.ROOT))
                .build()
                .start();
        // {{end:restServer}}

        // {{start:contentTypesServer}}
        Undertow.builder()
                .addHttpListener(8081, "0.0.0.0", Middleware.common(ContentTypesServer.ROUTES))
                .build()
                .start();
        // {{end:contentTypesServer}}

        // {{start:combinedServer}}
        RoutingHandler combinedHanlder = new RoutingHandler().addAll(RestServer.ROUTES)
                                                             .addAll(ContentTypesServer.ROUTES);
        Undertow.builder()
                .addHttpListener(8082, "0.0.0.0", Middleware.common(combinedHanlder))
                .build()
                .start();
        // {{end:combinedServer}}

        // {{start:multiPortServer}}
        Undertow.builder()
                .addHttpListener(8083, "0.0.0.0", Middleware.common(RestServer.ROOT))
                .addHttpListener(8084, "0.0.0.0", Middleware.common(ContentTypesServer.ROUTES))
                .build()
                .start();
        // {{end:multiPortServer}}

        // {{start:microserviceService}}
        Undertow.builder()
                .addHttpListener(8085, "0.0.0.0", exchange -> {
            Integer port = Exchange.queryParams()
                                   .queryParamAsInteger(exchange, "port")
                                   .orElse(null);
            if (port != null) {
                try {
                    HttpHandler handler = new ConstantStringHandler("web server with port " + port);
                    Undertow.builder()
                            .addHttpListener(port, "0.0.0.0", handler)
                            .build()
                            .start();
                } catch (Exception e) {
                    String message = "error trying to create web sertver with port " + port;
                    Exchange.body().sendText(exchange, message);
                    return;
                }
                Exchange.body().sendText(exchange, "server with port " + port + " created");
                return;
            }
            Exchange.body().sendText(exchange, "port cannot be null");
        })
        .build()
        .start();
        // {{end:microserviceService}}
    }
}
