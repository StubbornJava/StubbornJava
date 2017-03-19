package com.stubbornjava.examples.undertow.parameters;

import java.util.stream.LongStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.examples.common.HashIds;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class ParametersServer {
    private static final Logger log = LoggerFactory.getLogger(ParametersServer.class);

    // {{start:handlers}}
    private static void queryParam(HttpServerExchange exchange) {
        String name = Exchange.queryParams().queryParam(exchange, "name").orElse("world");
        int num = Exchange.queryParams().queryParamAsInteger(exchange, "num").orElse(1);
        Exchange.body().sendText(exchange, "Hello " + name + Strings.repeat("!", num));
    }

    private static void pathParam(HttpServerExchange exchange) {
        String name = Exchange.pathParams().pathParam(exchange, "name").orElse("world");
        int num = Exchange.pathParams().pathParamAsInteger(exchange, "num").orElse(1);
        Exchange.body().sendText(exchange, "Hello " + name + Strings.repeat("!", num));
    }
    // {{end:handlers}}

    // {{start:obfuscatedRoute}}
    private static void obfuscatedIdRoute(HttpServerExchange exchange) {
        // Using the above helper
        Long userId = ParametersServer.userId(exchange);
        // This is just to show the raw value.
        String rawUserIdParam = Exchange.pathParams()
                                        .pathParam(exchange, "userId")
                                        .orElse(null);
        Exchange.body().sendText(exchange, "UserId: " + userId + " hashed: " + rawUserIdParam);
    }
    // {{end:obfuscatedRoute}}

    // {{start:obfuscated}}
    /*
     * It's useful to create helpers for params you know you will use over and over.
     * It will reduce boilerpalte, typos, and ensure you use common defaults.
     * You could even add validation at this step if you wanted.
     */
    private static Long userId(HttpServerExchange exchange) {
        return Exchange.pathParams()
                       .pathParam(exchange, "userId")
                       .map(HashIds::decode)
                       .orElse(null);
    }
    // {{end:obfuscated}}

    // {{start:routes}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/hello", ParametersServer::queryParam)
        .get("/hello/{name}/{num}", ParametersServer::pathParam)
        .get("/users/{userId}", ParametersServer::obfuscatedIdRoute)
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
        // Just some examples for obfuscated parameters.
        LongStream.range(100_000_000, 100_000_003).forEach( id -> {
           log.debug("id: " + id + " hashed: " + HashIds.encode(id));
        });
        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        server.start();
    }
    // {{end:server}}
}
