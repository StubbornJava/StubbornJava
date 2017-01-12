package com.stubbornjava.undertow.exchange;

import java.nio.ByteBuffer;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public interface Json {
    default void sendString(HttpServerExchange exchange, String json) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(json);
    }

    default void sendBytes(HttpServerExchange exchange, byte[] bytes) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }
}
