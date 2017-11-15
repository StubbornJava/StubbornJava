package com.stubbornjava.examples.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogbackExamples {
    // {{start:slf4j}}
    /*
     *  Loggers are thread safe so it is okay to make them static.
     *  Sometimes you may want to pass instances though, it's up to you.
     */
    private static final Logger logger = LoggerFactory.getLogger(LogbackExamples.class);
    private static final Logger secretLogger = LoggerFactory.getLogger("com.stubbornjava.secrets.MySecretPasswordClass");

    public static void logLevels() {
        logger.trace("TRACE");
        logger.info("INFO");
        logger.debug("DEBUG");
        logger.warn("WARN");
        logger.error("ERROR");

        secretLogger.trace("TRACE");
        secretLogger.info("INFO");
        secretLogger.debug("DEBUG");
        secretLogger.warn("WARN");
        secretLogger.error("ERROR");
    }
    // {{end:slf4j}}

    // {{start:slf4jFormat}}
    public static void logFormat() {
        logger.info("Hello {}", "world");

        for (int i = 0; i < 5; i++) {
            logger.info("Hello {} i={}", "world", i);
        }
    }
    // {{end:slf4jFormat}}

    // {{start:slf4jConditionalLogging}}
    public static void conditionalLogging() {

        if (logger.isInfoEnabled()) {
            Object expensiveCall = null;
            logger.info("Logger expensive call {}", expensiveCall);
        }

        if (secretLogger.isInfoEnabled()) {
            Object expensiveCall = null;
            logger.info("Secret expensive call {}", expensiveCall);
        }
    }
    // {{end:slf4jConditionalLogging}}


    // {{start:slf4jException}}
    public static void logException() {
        try {
            throw new RuntimeException("What happened?");
        } catch (Exception ex) {
            logger.warn("Something bad happened", ex);
            logger.warn("Something bad happened with id: {}", 1, ex);
        }
    }
    // {{end:slf4jException}}

    public static void main(String[] args) {
        logLevels();
        logFormat();
        conditionalLogging();
        logException();
    }
}

