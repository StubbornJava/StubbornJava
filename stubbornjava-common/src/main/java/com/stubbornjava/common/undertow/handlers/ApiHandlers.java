package com.stubbornjava.common.undertow.handlers;

import com.stubbornjava.common.exceptions.ApiException;
import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;

public class ApiHandlers {

    public static void notFound(HttpServerExchange exchange, String message) {
        ApiError error = new ApiError(404, message);
        exchange.setStatusCode(error.getStatusCode());
        Exchange.body().sendJson(exchange, error);
    }

    public static void badRequest(HttpServerExchange exchange, String message) {
        ApiError error = new ApiError(400, message);
        exchange.setStatusCode(error.getStatusCode());
        Exchange.body().sendJson(exchange, error);
    }

    public static void serverError(HttpServerExchange exchange) {
        ApiError error = new ApiError(500, "Internal Server Error");
        exchange.setStatusCode(error.getStatusCode());
        Exchange.body().sendJson(exchange, error);
    }

    public static void handleApiException(HttpServerExchange exchange) {
        ApiException ex = (ApiException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Exchange.body().sendJson(exchange, new ApiError(ex.getStatusCode(), ex.getMessage()));
    }

    private static class ApiError {
        private final int statusCode;
        private final String message;
        public ApiError(int statusCode, String message) {
            super();
            this.statusCode = statusCode;
            this.message = message;
        }
        public int getStatusCode() {
            return statusCode;
        }
        public String getMessage() {
            return message;
        }
    }
}
