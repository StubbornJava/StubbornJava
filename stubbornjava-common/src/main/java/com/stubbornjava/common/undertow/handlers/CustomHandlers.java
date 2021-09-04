package com.stubbornjava.common.undertow.handlers;


import java.io.File;
import java.nio.file.Paths;
import java.util.Set;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck.Result;
import com.stubbornjava.common.AssetsConfig;
import com.stubbornjava.common.Env;
import com.stubbornjava.common.HealthChecks;
import com.stubbornjava.common.Metrics;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.undertow.handlers.MiddlewareBuilder;
import com.stubbornjava.undertow.handlers.ReferrerPolicyHandlers;
import com.stubbornjava.undertow.handlers.ReferrerPolicyHandlers.ReferrerPolicy;
import com.stubbornjava.undertow.handlers.StrictTransportSecurityHandlers;
import com.stubbornjava.undertow.handlers.XContentTypeOptionsHandler;
import com.stubbornjava.undertow.handlers.XFrameOptionsHandlers;
import com.stubbornjava.undertow.handlers.XXssProtectionHandlers;
import com.stubbornjava.undertow.handlers.accesslog.Slf4jAccessLogReceiver;

import io.undertow.Handlers;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.cache.DirectBufferCache;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.CachingResourceManager;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.util.Headers;
import okhttp3.HttpUrl;

public class CustomHandlers {
    private static final Logger log = LoggerFactory.getLogger(CustomHandlers.class);

    public static AccessLogHandler accessLog(HttpHandler next, Logger logger) {
        // see http://undertow.io/javadoc/2.0.x/io/undertow/server/handlers/accesslog/AccessLogHandler.html
        String format = "%H %h %u \"%r\" %s %Dms %b bytes \"%{i,Referer}\" \"%{i,User-Agent}\"";
        return new AccessLogHandler(next, new Slf4jAccessLogReceiver(logger), format, CustomHandlers.class.getClassLoader());
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
                      Predicates.truePredicate()
                      //Predicates.maxContentSize(20) // https://issues.jboss.org/browse/UNDERTOW-1234
                      ))
                  .setNext(next);
    }

    public static HttpHandler resource(String prefix, int cacheTime) {
        ResourceManager resourceManager = null;
        if (Env.LOCAL == Env.get()) {
            String path = Paths.get(AssetsConfig.assetsRoot(), prefix).toString();
            log.debug("using local file resource manager {}", path);
            resourceManager = new FileResourceManager(new File(path), 1024L * 1024L);
        } else {
            log.debug("using classpath file resource manager");
            ResourceManager classPathManager = new ClassPathResourceManager(CustomHandlers.class.getClassLoader(), prefix);
            resourceManager =
                    new CachingResourceManager(100, 65536,
                                               new DirectBufferCache(1024, 10, 10480),
                                               classPathManager,
                                               cacheTime);
        }
        ResourceHandler handler = new ResourceHandler(resourceManager);
        handler.setCacheTime(cacheTime);
        return handler;
    }

    public static StatusCodeHandler statusCodeMetrics(HttpHandler next) {
        return new StatusCodeHandler(next, "status.code");
    }

    public static TimingHttpHandler timed(String name, HttpHandler next) {
        return new TimingHttpHandler(next, "routes." + name);
    }

    public static void metrics(HttpServerExchange exchange) {
        Exchange.body().sendJson(exchange, Metrics.registry());
    }

    public static HttpHandler redirectToHost(String host) {
        return exchange -> {
            HttpUrl url = Exchange.urls().currentUrl(exchange);
            Exchange.redirect().permanent(exchange, url.newBuilder().host(host).build().toString());
        };
    }

    // {{start:health}}
    public static void health(HttpServerExchange exchange) {
        SortedMap<String, Result> results = HealthChecks.getHealthCheckRegistry().runHealthChecks();
        boolean unhealthy = results.values().stream().anyMatch(result -> !result.isHealthy());

        if (unhealthy) {
            /*
             *  Set a 500 status code also. A lot of systems / dev ops tools can
             *  easily test status codes but are not set up to parse JSON.
             *  Let's keep it simple for everyone.
             */
            exchange.setStatusCode(500);
        }
        Exchange.body().sendJson(exchange, results);
    }
    // {{end:health}}

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

    public static HttpHandler loadBalancerHttpToHttps(HttpHandler next) {
        return (HttpServerExchange exchange) -> {
            HttpUrl currentUrl = Exchange.urls().currentUrl(exchange);
            String protocolForward = Exchange.headers().getHeader(exchange, "X-Forwarded-Proto").orElse(null);
            if (null != protocolForward && protocolForward.equalsIgnoreCase("http")) {
                log.debug("non https switching to https {}", currentUrl.host());
                HttpUrl newUrl = currentUrl.newBuilder()
                   .scheme("https")
                   .port(443)
                   .build();
                exchange.setStatusCode(301);
                exchange.getResponseHeaders().put(Headers.LOCATION, newUrl.toString());
                exchange.endExchange();
                return;
            }

            next.handleRequest(exchange);
        };
    }

    // {{start:securityHeaders}}
    public static HttpHandler securityHeaders(HttpHandler next, ReferrerPolicy policy) {
        MiddlewareBuilder security = MiddlewareBuilder
            .begin(XFrameOptionsHandlers::deny)
            .next(XXssProtectionHandlers::enableAndBlock)
            .next(XContentTypeOptionsHandler::nosniff)
            .next(handler -> ReferrerPolicyHandlers.policy(handler, policy));

        // TODO: Only add HSTS if we are not local. We should probably
        // use a self signed cert locally for a better test env
        if (Env.LOCAL != Env.get()) {
            security = security.next(handler -> StrictTransportSecurityHandlers.hstsIncludeSubdomains(handler, 31536000L));
        }
        return security.complete(next);
    }
    // {{end:securityHeaders}}

    public static HttpHandler corsOriginWhitelist(HttpHandler next, Set<String> originWhitelist) {
        return exchange -> {
            String origin = Exchange.headers()
                                    .getHeader(exchange, Headers.ORIGIN)
                                    .orElse("");
            if (originWhitelist.contains(origin)) {
                Exchange.headers().setHeader(exchange, "Access-Control-Allow-Origin", origin);
            }
            next.handleRequest(exchange);
        };
    }
}
