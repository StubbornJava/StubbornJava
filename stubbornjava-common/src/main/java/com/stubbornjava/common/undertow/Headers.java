package com.stubbornjava.common.undertow;

import java.util.Optional;

import io.undertow.attribute.RequestHeaderAttribute;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public interface Headers {

    default Optional<String> getHeader(HttpServerExchange exchange, HttpString header) {
        RequestHeaderAttribute reqHeader = new RequestHeaderAttribute(header);
        return Optional.ofNullable(reqHeader.readAttribute(exchange));
    }

    default Optional<String> getHeader(HttpServerExchange exchange, String header) {
        RequestHeaderAttribute reqHeader = new RequestHeaderAttribute(new HttpString(header));
        return Optional.ofNullable(reqHeader.readAttribute(exchange));
    }

    default void setHeader(HttpServerExchange exchange, HttpString header, String value) {
        exchange.getResponseHeaders().add(header, value);
    }

    default void setHeader(HttpServerExchange exchange, String header, String value) {
        exchange.getResponseHeaders().add(new HttpString(header), value);
    }
}
