package com.stubbornjava.examples.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// {{start:logback}}
public class LogbackExamples {
    /*
     *  Loggers are thread safe so it is okay to make them static.
     *  Sometimes you may want to instances though, it's up to you.
     */
    private static final Logger logger = LoggerFactory.getLogger(LogbackExamples.class);
    private static final Logger secretLogger = LoggerFactory.getLogger("com.stubbornjava.secrets.MySecretPasswordClass");

    public static void main(String[] args) {

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
}
// {{end:logback}}

