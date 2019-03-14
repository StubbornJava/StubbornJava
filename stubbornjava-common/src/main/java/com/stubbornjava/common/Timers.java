package com.stubbornjava.common;


import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

public class Timers {
    private static final Logger logger = LoggerFactory.getLogger(Timers.class);

    private Timers() {}

    public static void time(String message, Runnable runnable) {
        Stopwatch sw = Stopwatch.createStarted();
        try {
            logger.info("{}", message);
            runnable.run();
        } catch (Exception ex) {
            logger.warn("Exception in runnable", ex);
            throw ex;
        } finally {
            logger.info("{} took {}ms", message, sw.elapsed(TimeUnit.MILLISECONDS));
        }
    }

}
