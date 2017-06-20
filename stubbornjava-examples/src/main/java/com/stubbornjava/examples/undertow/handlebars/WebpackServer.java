package com.stubbornjava.examples.undertow.handlebars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.stubbornjava.common.SimpleResponse;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.undertow.handlers.MiddlewareBuilder;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;

public class WebpackServer {
    private static final Logger log = LoggerFactory.getLogger(WebpackServer.class);

    public static void notFound(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/notfound", SimpleResponse.create());
    }

    public static void error(HttpServerExchange exchange) {
        exchange.setStatusCode(500);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/serverError", SimpleResponse.create());
    }

    // Render homepage
    public static void home(HttpServerExchange exchange) {
        exception(exchange);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/home", SimpleResponse.create());
    }

    // Render hello {name} page.
    public static void hello(HttpServerExchange exchange) {
        exception(exchange);
        String name = Exchange.queryParams()
                              .queryParam(exchange, "name")
                              .filter(s -> !Strings.isNullOrEmpty(s))
                              .orElse("world");
        SimpleResponse response = SimpleResponse.create()
                                                .with("name", name);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/hello", response);
    }

    private static void exception(HttpServerExchange exchange) {
        if (Exchange.queryParams().queryParamAsBoolean(exchange, "exception").orElse(false)) {
            throw new RuntimeException("Poorly Named Exception!!!");
        }
    }

    // {{start:server}}
    private static HttpHandler exceptionHandler(HttpHandler next) {
        return CustomHandlers.exception(next)
           .addExceptionHandler(Throwable.class, WebpackServer::error);
    }

    private static HttpHandler wrapWithMiddleware(HttpHandler handler) {
        return MiddlewareBuilder.begin(BlockingHandler::new)
                                .next(CustomHandlers::gzip)
                                .next(ex -> CustomHandlers.accessLog(ex, log))
                                .next(CustomHandlers::statusCodeMetrics)
                                .next(WebpackServer::exceptionHandler)
                                .complete(handler);
    }

    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/", WebpackServer::home)
        .get("/hello", WebpackServer::hello)
        .get("/static*", CustomHandlers.resource(""))
        .setFallbackHandler(WebpackServer::notFound)
    ;

    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(wrapWithMiddleware(ROUTES));
        server.start();
    }
    // {{end:server}}
}
