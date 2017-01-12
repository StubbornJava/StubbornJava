package com.stubbornjava.examples.undertow.exceptionhandling;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.examples.undertow.exceptionhandling.ExceptionHandlingServer.ApiException;
import com.stubbornjava.examples.undertow.exceptionhandling.ExceptionHandlingServer.WebException;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;

public class ExceptionHandlers {

    // {{start:exceptionhandlers}}
    public static void handleWebException(HttpServerExchange exchange) {
        WebException ex = (WebException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Exchange.body().sendHtml(exchange, "<h1>" + ex.getMessage() + "</h1>");
    }

    public static void handleApiException(HttpServerExchange exchange) {
        ApiException ex = (ApiException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Exchange.body().sendJson(exchange, "{\"message\": \"" + ex.getMessage() + "\"}");
    }

    public static void handleAllExceptions(HttpServerExchange exchange) {
        exchange.setStatusCode(500);
        Exchange.body().sendText(exchange, "Internal Server Error!");
    }
    // {{end:exceptionhandlers}}

    // {{start:handlers}}
    public static void throwWebException(HttpServerExchange exchange) {
        throw new WebException(500, "Web Server Error");
    }

    public static void throwApiException(HttpServerExchange exchange) {
        throw new ApiException(503, "API Server Error");
    }

    public static void throwException(HttpServerExchange exchange) {
        throw new RuntimeException();
    }

    public static void ok(HttpServerExchange exchange) {
        Exchange.body().sendText(exchange, "ok");
    }
    // {{end:handlers}}
}
