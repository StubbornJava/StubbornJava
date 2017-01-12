package com.stubbornjava.undertow.response;

import java.nio.ByteBuffer;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public interface ContentTypeSenders {

    default void json(HttpServerExchange exchange, String json) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(json);
    }

    default void json(HttpServerExchange exchange, byte[] bytes) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }

    default void html(HttpServerExchange exchange, String html) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(html);
    }

    default void text(HttpServerExchange exchange, String text) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(text);
    }

    default void file(HttpServerExchange exchange, String fileName, String content) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseHeaders().put(Headers.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
        exchange.getResponseSender().send(content);
    }

    default void file(HttpServerExchange exchange, String fileName, byte[] bytes) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseHeaders().put(Headers.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }
}
