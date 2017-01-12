package com.stubbornjava.examples.undertow.contenttypes;

import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class ContentTypeHandlers {
    private static final String fileName = "helloworld.txt";

    // {{start:text}}
    // @WhereAreAllMyAnnotations??
    // @DecoupledLogicFromRoutingIsNice!
    public static void helloWorldText(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World");
    }
    // {{end:text}}

    // {{start:html}}
    public static void helloWorldHtml(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send("<h1>Hello World</h1>");
    }
    // {{end:html}}

    // {{start:file}}
    public static void helloWorldFileDownload(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseHeaders().put(Headers.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
        exchange.getResponseSender().send("Hello World");
    }
    // {{end:file}}

    // {{start:json}}
    public static void helloWorldJson(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send("{\"message\": \"Hello World\"}");
    }
    // {{end:json}}

    // {{start:senders}}
    public static void helloWorldJsonSender(HttpServerExchange exchange) {
        Exchange.body().sendJson(exchange, "{\"message\": \"Hello World\"}");
    }
    // {{end:senders}}
}
