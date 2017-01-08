package com.stubbornjava.examples.undertow.contenttypes;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class ContentTypeHandlers {
    private static final String fileName = "helloworld.txt";

    // @WhereAreAllMyAnnotations??
    // @DecoupledLogicFromRoutingIsNice!
    public static void helloWorldText(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World");
    }

    public static void helloWorldHtml(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send("<h1>Hello World</h1>");
    }

    public static void helloWorldFileDownload(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseHeaders().put(Headers.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
        exchange.getResponseSender().send("Hello World");
    }

    public static void helloWorldJson(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send("{\"message\": \"Hello World\"}");
    }
}
