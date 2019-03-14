package com.stubbornjava.common.undertow.handlers.diagnostic;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jooq.lambda.Seq;
import org.jooq.lambda.Unchecked;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.MoreExecutors;
import com.stubbornjava.common.Http;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.UndertowUtil;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.undertow.handlers.MiddlewareBuilder;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class DelayedExecutionHandlerTest {

    // Delay for 500ms then return "ok"
    private static final DelayedExecutionHandler delayedHandler =
        DiagnosticHandlers.fixedDelay((exchange) -> {
                Exchange.body().sendText(exchange, "ok");
            },
            500, TimeUnit.MILLISECONDS);

    @Test
    public void testOnXIoThread() throws InterruptedException {
        int numThreads = 10;
        run(delayedHandler, numThreads);
    }

    @Test
    public void testOnWorkerThread() throws InterruptedException {
        int numThreads = 10;
        run(new BlockingHandler(delayedHandler), numThreads);
    }

    /**
     * Spin up a new server with a single IO thread and worker thread.
     * Run N GET requests against it concurrently and make sure they
     * do not take N * 500ms total. This is not the best test but it
     * should show that we are delaying N requests at once using a single
     * thread.
     *
     * @param handler
     * @param numThreads
     * @throws InterruptedException
     */
    private void run(HttpHandler handler, int numThreads) throws InterruptedException {
        HttpHandler route = MiddlewareBuilder.begin(CustomHandlers::accessLog)
                                                  .complete(handler);
        Undertow.Builder builder = Undertow.builder()
            .setWorkerThreads(1)
            .setIoThreads(1);
        UndertowUtil.useLocalServer(builder, route, host -> {
            ExecutorService exec = Executors.newFixedThreadPool(numThreads);
            OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(HttpClient.getLoggingInterceptor())
                .build();

            // Using time in tests isn't the best approach but this one seems
            // A little difficult to test another way.
            Stopwatch sw = Stopwatch.createStarted();
            List<Callable<Response>> callables = IntStream.range(0, numThreads)
                 .mapToObj(i -> (Callable<Response>) () -> Http.get(client, host))
                 .collect(Collectors.toList());
            sw.stop();
            Seq.seq(Unchecked.supplier(() -> exec.invokeAll(callables)).get())
               .map(Unchecked.function(Future::get))
               .forEach(DelayedExecutionHandlerTest::assertSuccess);
            assertTrue("Responses took too long", sw.elapsed().toMillis() < 1_000);
            MoreExecutors.shutdownAndAwaitTermination(exec, 10, TimeUnit.SECONDS);
        });
    }

    private static void assertSuccess(Response response) {
       Assert.assertTrue("Response should be a 200", response.isSuccessful());
    }

}
