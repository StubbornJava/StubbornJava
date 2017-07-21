package com.stubbornjava.webapp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.Templating;
import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;

/**
 * Created by billoneil on 8/29/16.
 */
public class PageRoutes {
    private static final Logger logger = LoggerFactory.getLogger(PageRoutes.class);

    public static HttpHandler redirector(HttpHandler next) {
        return (HttpServerExchange exchange) -> {
            HttpUrl currentUrl = Exchange.urls().currentUrl(exchange);
            String protocolForward = Exchange.headers().getHeader(exchange, "X-Forwarded-Proto").orElse(null);
            String host = currentUrl.host();
            boolean redirect = false;

            Builder newUrlBuilder = currentUrl.newBuilder();

            if (host.equals("stubbornjava.com")) {
                host = "www." + host;
                newUrlBuilder.host(host);
                redirect = true;
                logger.debug("Host {} does not start with www redirecting to {}", currentUrl.host(), host);
            }

            if (null != protocolForward && protocolForward.equalsIgnoreCase("http")) {
                logger.debug("non https switching to https", currentUrl.host(), host);
                newUrlBuilder.scheme("https")
                             .port(443);
                redirect = true;
            }

            if (redirect) {
                HttpUrl newUrl = newUrlBuilder.build();
                exchange.setStatusCode(301);
                exchange.getResponseHeaders().put(Headers.LOCATION, newUrl.toString());
                exchange.endExchange();
                return;
            }
            next.handleRequest(exchange);
        };
    }

    public static void ping(HttpServerExchange exchange) {
        Exchange.body().sendText(exchange, "OK");
    }

    public static void home(HttpServerExchange exchange) {
        Response response = Response.fromExchange(exchange)
                                    .withRecentPosts(25)
                                    .withLibCounts()
                                    .withTagCounts();
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/home", response);
    }

    // {{start:robots}}
    public static void robots(HttpServerExchange exchange) {
        String host = Exchange.urls().host(exchange).toString();
        List<String> sitemaps = StubbornJavaSitemapGenerator.getSitemap().getIndexNames();
        Response response = Response.fromExchange(exchange)
                                    .with("sitemaps", sitemaps)
                                    .with("host", host);
        Exchange.body().sendText(exchange, Templating.instance().renderTemplate("templates/src/pages/robots.txt", response));
    }
    // {{end:robots}}

    public static void notFound(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/notFound", Response.fromExchange(exchange));
    }

    public static void error(HttpServerExchange exchange) {
        exchange.setStatusCode(500);
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/serverError", Response.fromExchange(exchange));
    }
}
