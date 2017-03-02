package com.stubbornjava.common.undertow.handlers;


import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.AssetsConfig;
import com.stubbornjava.common.Env;
import com.stubbornjava.common.Metrics;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.undertow.handlers.accesslog.Slf4jAccessLogReceiver;

import io.undertow.Handlers;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;

public class CustomHandlers {
    private static final Logger log = LoggerFactory.getLogger(CustomHandlers.class);

    public static AccessLogHandler accessLog(HttpHandler next, Logger logger) {
        return new AccessLogHandler(next, new Slf4jAccessLogReceiver(logger), "combined", CustomHandlers.class.getClassLoader());
    }

    public static AccessLogHandler accessLog(HttpHandler next) {
        final Logger logger = LoggerFactory.getLogger("com.stubbornjava.accesslog");
        return accessLog(next, logger);
    }

    public static HttpHandler gzip(HttpHandler next) {
        return new EncodingHandler(new ContentEncodingRepository()
                  .addEncodingHandler("gzip",
                      // This 1000 is a priority, not exactly sure what it does.
                      new GzipEncodingProvider(), 1000,
                      // Anything under a content-length of 20 will not be gzipped
                      Predicates.parse("max-content-size(20)")))
                  .setNext(next);
    }

    public static HttpHandler resource(String prefix) {
        ResourceManager resourceManager = null;
        if (Env.LOCAL == Env.get()) {
            log.debug("using local file resource manager {}", AssetsConfig.assetsRoot() + prefix);
            resourceManager = new FileResourceManager(new File(AssetsConfig.assetsRoot() + prefix), 1024 * 1024);
        } else {
            log.debug("using classpath file resource manager");
            resourceManager = new ClassPathResourceManager(CustomHandlers.class.getClassLoader(), prefix);
        }
        ResourceHandler handler = new ResourceHandler(resourceManager);
        handler.setCacheTime((int)TimeUnit.HOURS.toSeconds(4));
        return handler;
    }

    public static StatusCodeHandler statusCodeMetrics(HttpHandler next) {
        return new StatusCodeHandler(next, "status.code");
    }

    public static TimingHttpHandler timed(String name, HttpHandler next) {
        return new TimingHttpHandler(next, name);
    }

    public static void metrics(HttpServerExchange exchange) {
        Exchange.body().sendJson(exchange, Metrics.registry());
    }

    public static ExceptionHandler exception(HttpHandler handler) {
        return Handlers.exceptionHandler((HttpServerExchange exchange) -> {
            try {
                handler.handleRequest(exchange);
            } catch (Throwable th) {
                log.error("exception thrown at " + exchange.getRequestURI(), th);
                throw th;
            }
        });
    }
}
