package com.stubbornjava.undertow.exchange;

import java.util.Deque;
import java.util.Optional;

import io.undertow.server.HttpServerExchange;

public interface PathParams {

    default String pathParam(HttpServerExchange exchange, String name) {
        return Optional.ofNullable(exchange.getPathParameters().get(name))
                       .map(Deque::getFirst)
                       .orElseThrow(() -> new RuntimeException("Missing required path param " + name));
    }

    default Long pathParamAsLong(HttpServerExchange exchange, String name) {
        return Optional.ofNullable(exchange.getPathParameters().get(name))
                .map(Deque::getFirst)
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Missing required path param " + name));
    }

    default Integer pathParamAsInteger(HttpServerExchange exchange, String name) {
        return Optional.ofNullable(exchange.getPathParameters().get(name))
                .map(Deque::getFirst)
                .map(Integer::parseInt)
                .orElseThrow(() -> new RuntimeException("Missing required path param " + name));
    }
}
