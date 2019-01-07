package com.stubbornjava.common.undertow.handlers.diagnostic;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.stubbornjava.common.undertow.handlers.diagnostic.DelayedExecutionHandler.Duration;

import io.undertow.server.HttpHandler;

public class DiagnosticHandlers {

    // {{start:delayedHandler}}
    /**
     * Add a fixed delay before execution of the next handler
     * @param next
     * @param duration
     * @param unit
     * @return
     */
    public static DelayedExecutionHandler fixedDelay(HttpHandler next,
                                                     long duration,
                                                     TimeUnit unit) {
        return new DelayedExecutionHandler(
            next, (exchange) -> new Duration(duration, unit));
    }

    /**
     * Add a random delay between minDuration (inclusive) and
     * maxDuration (exclusive) before execution of the next handler.
     * This can be used to add artificial latency for requests.
     *
     * @param next
     * @param minDuration inclusive
     * @param maxDuration exclusive
     * @param unit
     * @return
     */
    public static DelayedExecutionHandler randomDelay(HttpHandler next,
                                                     long minDuration,
                                                     long maxDuration,
                                                     TimeUnit unit) {
    return new DelayedExecutionHandler(
        next, (exchange) ->  {
            long duration = ThreadLocalRandom.current()
                                             .nextLong(minDuration, maxDuration);
            return new Duration(duration, unit);
        });
    }
    // {{end:delayedHandler}}
}
