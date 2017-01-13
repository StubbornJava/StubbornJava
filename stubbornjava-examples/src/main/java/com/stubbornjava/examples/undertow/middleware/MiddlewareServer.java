package com.stubbornjava.examples.undertow.middleware;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import com.stubbornjava.common.Metrics;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.examples.undertow.exceptionhandling.ExceptionHandlers;
import com.stubbornjava.undertow.handlers.MiddlewareBuilder;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;

public class MiddlewareServer {
    // {{start:handlers}}
    private static void textFast(HttpServerExchange exchange) {
        Exchange.body().sendText(exchange, "Fast!");
    }

    private static void textSlow(HttpServerExchange exchange) {
        // Sure whatever just sleep.
        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Exchange.body().sendText(exchange, "Slow!!!");
    }

    private static void throwException(HttpServerExchange exchange) {
        throw new RuntimeException("Error, I hope you handled it!");
    }

    private static void notFound(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        Exchange.body().sendText(exchange, "Not Found");
    }

    private static void metrics(HttpServerExchange exchange) {
        Exchange.body().sendJson(exchange, Metrics.registry());
    }
    // {{end:handlers}}

    // {{start:routes}}
    /*
     * Notice how we have route level middleware here with the timed HttpHandler.
     */
    private static final HttpHandler ROUTES = new RoutingHandler()
        .get("/textFast", timed("textFast", MiddlewareServer::textFast))
        .get("/textSlow", timed("textSlow", MiddlewareServer::textSlow))
        .get("/throwException", timed("throwException", MiddlewareServer::throwException))
        .get("/metrics", timed("metrics", MiddlewareServer::metrics))
        .setFallbackHandler(timed("notFound", MiddlewareServer::notFound))
    ;
    // {{end:routes}}


    // {{start:middleware}}
    private static HttpHandler exceptionHandler(HttpHandler next) {
        return Handlers.exceptionHandler(next)
           .addExceptionHandler(Throwable.class, ExceptionHandlers::handleAllExceptions);
    }

    private static HttpHandler wrapWithMiddleware(HttpHandler handler) {
        /*
         * Undertow has I/O threads for handling inbound connections and non blocking IO.
         * If you are doing any blocking you should use the BlockingHandler. This
         * will push work into a separate Executor with customized thread pool
         * which is made for blocking work. I am blocking immediately here because I am lazy.
         * Don't block if you don't need to. Remember you can configure only certain routes block.
         * When looking at logs you can tell if you are blocking or not by the thread name.
         * I/O non blocking thread - [XNIO-1 I/O-{threadnum}] - You should NOT be blocking here.
         * Blocking task threads - [XNIO-1 task-{threadnum}] This pool is made for blocking.
         */
        return MiddlewareBuilder.begin(BlockingHandler::new)
                                .next(CustomHandlers::gzip)
                                .next(CustomHandlers::accessLog)
                                .next(CustomHandlers::statusCodeMetrics)
                                .next(MiddlewareServer::exceptionHandler)
                                .complete(handler);
    }
    // {{end:middleware}}

    // {{start:server}}
    public static void main(String[] args) {
        // Wrapping routes with middleware here!
        SimpleServer server = SimpleServer.simpleServer(wrapWithMiddleware(ROUTES));
        server.start();
    }
    // {{end:server}}
}
