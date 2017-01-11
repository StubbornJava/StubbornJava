package com.stubbornjava.examples.undertow.exceptionhandling;

import com.stubbornjava.common.server.Senders;
import com.stubbornjava.examples.undertow.exceptionhandling.ExceptionHandlingServer.ApiException;
import com.stubbornjava.examples.undertow.exceptionhandling.ExceptionHandlingServer.WebException;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;

public class ExceptionHandlers {

    // {{start:exceptionhandlers}}
    public static void handleWebException(HttpServerExchange exchange) {
        WebException ex = (WebException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Senders.send().html(exchange, "<h1>" + ex.getMessage() + "</h1>");
    }

    public static void handleApiException(HttpServerExchange exchange) {
        ApiException ex = (ApiException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Senders.send().json(exchange, "{\"message\": \"" + ex.getMessage() + "\"}");
    }

    public static void handleAllExceptions(HttpServerExchange exchange) {
        exchange.setStatusCode(500);
        Senders.send().text(exchange, "Internal Server Error!");
    }
    // {{end:exceptionhandlers}}

    // {{start:handlers}}
    public static void throwWebException(HttpServerExchange exchange) {
        throw new WebException(500, "Web Server Error");
    }

    public static void throwApiException(HttpServerExchange exchange) {
        throw new ApiException(503, "API Server Error");
    }
    // {{end:handlers}}
}
