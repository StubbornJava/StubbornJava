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
}
