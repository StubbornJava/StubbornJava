package com.stubbornjava.examples.undertow.parameters;

import com.google.common.base.Strings;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class ParametersServer {

    // {{start:handlers}}
    private static void queryParam(HttpServerExchange exchange) {
        String name = Exchange.queryParams().queryParam(exchange, "name").orElse("world");
        int num = Exchange.queryParams().queryParamAsInteger(exchange, "num").orElse(1);
        Exchange.body().sendText(exchange, "Hello " + name + Strings.repeat("!", num));
    }

    private static void pathParam(HttpServerExchange exchange) {
        String name = Exchange.pathParams().pathParam(exchange, "name");
        int num = Exchange.pathParams().pathParamAsInteger(exchange, "num");
        Exchange.body().sendText(exchange, "Hello " + name + Strings.repeat("!", num));
    }
    // {{end:handlers}}

    // {{start:routes}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/hello", ParametersServer::queryParam)
        .get("/hello/{name}/{age}", ParametersServer::pathParam)
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        server.start();
    }
    // {{end:server}}
}
