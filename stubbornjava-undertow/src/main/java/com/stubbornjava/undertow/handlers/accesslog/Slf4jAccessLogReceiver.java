package com.stubbornjava.undertow.handlers.accesslog;

import org.slf4j.Logger;

import io.undertow.server.handlers.accesslog.AccessLogReceiver;

public class Slf4jAccessLogReceiver implements AccessLogReceiver {
    private final Logger logger;

    public Slf4jAccessLogReceiver(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void logMessage(String message) {
        logger.info("{}", message);
    }
}
