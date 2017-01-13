package com.stubbornjava.undertow.exchange;

import java.util.Deque;
import java.util.Optional;

import io.undertow.server.HttpServerExchange;

public interface PathParams {

    default Optional<String> pathParam(HttpServerExchange exchange, String name) {
        /*
         *  I think there is a bug with path params in routing handler, will revisit.
         *  Luckily RoutingHandler by default puts all path params into the query params.
         */
        return Optional.ofNullable(exchange.getQueryParameters().get(name))
                       .map(Deque::getFirst);
    }

    default Optional<Long> pathParamAsLong(HttpServerExchange exchange, String name) {
        return pathParam(exchange, name).map(Long::parseLong);
    }

    default Optional<Integer> pathParamAsInteger(HttpServerExchange exchange, String name) {
        return pathParam(exchange, name).map(Integer::parseInt);
    }
}
