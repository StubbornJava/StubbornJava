package com.stubbornjava.examples.undertow.exceptionhandling;

import com.stubbornjava.common.undertow.SimpleServer;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class ExceptionHandlingServer {

    // {{start:routes}}
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/throwWebException", ExceptionHandlers::throwWebException)
        .get("/throwApiException", ExceptionHandlers::throwApiException)
        .get("/throwException", ExceptionHandlers::throwException)
        .get("/ok", ExceptionHandlers::ok)
    ;
    // {{end:routes}}

    // {{start:exceptionhandler}}
    /*
     * The Exception handler wraps the routing handler.
     * Undertow allows as little or as much flexibility as you
     * need through this style of composition.
     */
    private static final HttpHandler ROOT = Handlers.exceptionHandler(ROUTES)
        /*
         * The order here is import with exception subclassing.
         * Add the most restrictive classes first. If we put
         * Throwable first it would handle all of the exceptions.
         */
        .addExceptionHandler(ApiException.class, ExceptionHandlers::handleApiException)
        .addExceptionHandler(WebException.class, ExceptionHandlers::handleWebException)
        .addExceptionHandler(Throwable.class, ExceptionHandlers::handleAllExceptions)
    ;
    // {{end:exceptionhandler}}

    // {{start:exceptions}}
    public static class WebException extends RuntimeException {
        private final int statusCode;
        public WebException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    public static class ApiException extends WebException {
        public ApiException(int statusCode, String message) {
            super(statusCode, message);
        }
    }
    // {{end:exceptions}}

    // {{start:server}}
    public static void main(String[] args) {
        // Notice we pass ROOT here not ROUTES.
        SimpleServer server = SimpleServer.simpleServer(ROOT);
        server.start();
    }
    // {{end:server}}
}
