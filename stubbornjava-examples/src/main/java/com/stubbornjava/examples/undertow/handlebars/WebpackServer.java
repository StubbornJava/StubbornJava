package com.stubbornjava.examples.undertow.handlebars;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import java.util.concurrent.TimeUnit;

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

    // {{start:routes}}
    // Simple not found 404 page
    public static void notFound(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/notfound", SimpleResponse.create());
    }

    // Default error page when something unexpected happens
    public static void error(HttpServerExchange exchange) {
        exchange.setStatusCode(500);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/serverError", SimpleResponse.create());
    }

    // Render homepage
    public static void home(HttpServerExchange exchange) {
        exception(exchange);
        Exchange.body().sendHtmlTemplate(exchange, "static/templates/src/home", SimpleResponse.create());
    }

    // Render hello {name} page based on the name query param.
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

    // Helper function to forcibly throw an exception whenever the query
    // parameter exception=true
    private static void exception(HttpServerExchange exchange) {
        if (Exchange.queryParams().queryParamAsBoolean(exchange, "exception").orElse(false)) {
            throw new RuntimeException("Poorly Named Exception!!!");
        }
    }
    // {{end:routes}}

    // {{start:server}}
    // We are currently handling all exceptions the same way
    private static HttpHandler exceptionHandler(HttpHandler next) {
        return CustomHandlers.exception(next)
           .addExceptionHandler(Throwable.class, WebpackServer::error);
    }

    // Useful middleware
    private static HttpHandler wrapWithMiddleware(HttpHandler handler) {
        return MiddlewareBuilder.begin(BlockingHandler::new)
                                .next(CustomHandlers::gzip)
                                .next(ex -> CustomHandlers.accessLog(ex, log))
                                .next(CustomHandlers::statusCodeMetrics)
                                .next(WebpackServer::exceptionHandler)
                                .complete(handler);
    }

    // Simple routing, anything not matching a route will fall back
    // to the not found handler.
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/", timed("home", WebpackServer::home))
        .get("/hello", timed("hello", WebpackServer::hello))
        .get("/static*", timed("static", CustomHandlers.resource("", (int)TimeUnit.HOURS.toSeconds(4))))
        .setFallbackHandler(timed("notfound", WebpackServer::notFound))
    ;

    public static void main(String[] args) {
        SimpleServer server = SimpleServer.simpleServer(wrapWithMiddleware(ROUTES));
        server.start();
    }
    // {{end:server}}
}
