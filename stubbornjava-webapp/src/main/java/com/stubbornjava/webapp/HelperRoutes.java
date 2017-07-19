package com.stubbornjava.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.Metrics;
import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;

public class HelperRoutes {
    private static final Logger logger = LoggerFactory.getLogger(HelperRoutes.class);

    public static void getMetrics(HttpServerExchange exchange) {
        Exchange.body().sendJson(exchange, Metrics.registry());
    }

//    public static void getLoggers(HttpServerExchange exchange) {
//        Response data = Response.fromExchange(exchange).with("loggers", Logging.getLoggers());
//        Exchange.body().sendHtmlTemplate(exchange, "templates/loggers", data);
//    }

//    public static void getProperties(HttpServerExchange exchange) {
//        boolean hidden = RequestUtil.getQueryParam(exchange, "hidden")
//                                    .map(val -> !Strings.isNullOrEmpty(val))
//                                    .orElse(false);
//        RequestUtil.sendHtml(exchange, "static/html/properties.html",
//            new Response()
//                .with("properties", Config.propsAsMap()
//                                        .entrySet()
//                                        .stream()
//                                        .filter((entry) -> !entry.getKey().startsWith("db") || hidden)
//                                        .filter((entry) -> !entry.getKey().startsWith("hmac") || hidden)
//                                        .filter((entry) -> !entry.getKey().startsWith("jooq") || hidden)
//                                        .collect(Collectors.toList())));
//    }
//
//    public static void getSystem(HttpServerExchange exchange) {
//        RequestUtil.sendHtml(exchange, "static/html/properties.html",
//                Response.fromExchange(exchange)
//                    .with("properties", Config.systemAsMap().entrySet()));
//    }
}
