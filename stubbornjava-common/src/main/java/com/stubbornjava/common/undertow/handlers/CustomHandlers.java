package com.stubbornjava.common.undertow.handlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.undertow.handlers.accesslog.Slf4jAccessLogReceiver;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;

public class CustomHandlers {

    public static AccessLogHandler accessLog(HttpHandler next, Logger logger) {
        return new AccessLogHandler(next, new Slf4jAccessLogReceiver(logger), "combined", CustomHandlers.class.getClassLoader());
    }

    public static AccessLogHandler accessLog(HttpHandler next) {
        final Logger logger = LoggerFactory.getLogger("com.stubbornjava.accesslog");
        return accessLog(next, logger);
    }
}
