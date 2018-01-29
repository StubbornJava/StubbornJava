package com.stubbornjava.examples.failsafe;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CircuitBreakerHandler;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import net.jodah.failsafe.CircuitBreaker;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class FailsafeWebserver {
    private static final Logger log = LoggerFactory.getLogger(FailsafeWebserver.class);

    // {{start:handlers}}
    private static final void serverError(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        Exchange.body().sendText(exchange, "500 - Internal Server Error");
    }

    // This handler helps simulate errors, bad requests, and successful requests.
    private static final void circuitClosed(HttpServerExchange exchange) {
        boolean error = Exchange.queryParams().queryParamAsBoolean(exchange, "error").orElse(false);
        boolean exception = Exchange.queryParams().queryParamAsBoolean(exchange, "exception").orElse(false);
        if (error) {
            exchange.setStatusCode(StatusCodes.BAD_REQUEST);
            Exchange.body().sendText(exchange, "Bad Request");
        } else if (exception) {
            throw new RuntimeException("boom");
        } else {
            Exchange.body().sendText(exchange, "Circuit is open everything is functioning properly.");
        }
    }

    private static final HttpHandler CIRCUIT_BREAKER_HANDLER;
    static {
        CircuitBreaker breaker = new CircuitBreaker()
             // Trigger circuit breaker failure on exceptions or bad requests
            .failIf((HttpServerExchange exchange, Throwable ex) -> {
                return (exchange != null && StatusCodes.BAD_REQUEST == exchange.getStatusCode())
                        || ex != null;
            })
            // If 7 out of 10 requests fail Open the circuit
            .withFailureThreshold(7, 10)
            // When half open if 3 out of 5 requests succeed close the circuit
            .withSuccessThreshold(3, 5)
            // Delay this long before half opening the circuit
            .withDelay(2, TimeUnit.SECONDS)
            .onClose(() -> log.info("Circuit Closed"))
            .onOpen(() -> log.info("Circuit Opened"))
            .onHalfOpen(() -> log.info("Circuit Half-Open"));

        CIRCUIT_BREAKER_HANDLER = new CircuitBreakerHandler(breaker,
                                                            FailsafeWebserver::circuitClosed,
                                                            FailsafeWebserver::serverError);
    }
    // {{end:handlers}}

    // {{start:request}}
    private static void request(boolean error, boolean exception) {
        HttpUrl url = HttpUrl.parse("http://localhost:8080")
                             .newBuilder()
                             .addQueryParameter("error", String.valueOf(error))
                             .addQueryParameter("exception", String.valueOf(exception))
                             .build();

        Request request = new Request.Builder().get().url(url).build();
        try {
            log.info(HttpClient.globalClient().newCall(request).execute().body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // {{end:request}}

    // {{start:main}}
    public static void main(String[] args) {

        HttpHandler exceptionHandler =
                CustomHandlers.exception(CIRCUIT_BREAKER_HANDLER)
                              .addExceptionHandler(Throwable.class, FailsafeWebserver::serverError);

        SimpleServer server = SimpleServer.simpleServer(exceptionHandler);
        server.start();


        // Warm-up the circuit breaker it needs to hit at least max executions
        // Before it will reject anything. This will make that easier.
        for (int i = 0; i < 10; i++) {
            request(false, false);
        }
        ScheduledExecutorService schedExec = Executors.newScheduledThreadPool(1);

        // A simple request that should always succeed
        schedExec.scheduleAtFixedRate(() -> request(false, false), 0, 500, TimeUnit.MILLISECONDS);

        // Send a batch of 15 bad requests to trigger the circuit breaker
        Runnable errors = () -> {
            log.info("Executing bad requests!");
            for (int i = 0; i < 15; i++) {
                request(true, false);
            }
        };
        schedExec.schedule(errors, 1, TimeUnit.SECONDS);

        // Send a batch of 15 requests that throw exceptions
        Runnable exceptions = () -> {
            log.info("Executing requests that throw exceptions!");
            for (int i = 0; i < 15; i++) {
                request(false, true);
            }
        };
        schedExec.schedule(exceptions, 5, TimeUnit.SECONDS);
    }
    // {{end:main}}
}
