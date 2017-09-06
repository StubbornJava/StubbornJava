package com.stubbornjava.examples.common;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Suppliers;

public class SuppliersExamples {
    private static final Logger log = LoggerFactory.getLogger(SuppliersExamples.class);

    // {{start:supplier}}
    public static String helloWorldSupplier() {
        log.info("supplying");
        return "hello world";
    }
    // {{end:supplier}}

    public static void main(String[] args) throws InterruptedException {
        // {{start:memoize}}
        log.info("Memoized");
        Supplier<String> memoized = Suppliers.memoize(SuppliersExamples::helloWorldSupplier);
        log.info(memoized.get());
        log.info(memoized.get());
        // {{end:memoize}}

        // {{start:memoizeWithExpiration}}
        log.info("Memoized with Expiration");
        Supplier<String> memoizedExpiring = Suppliers.memoizeWithExpiration(
            SuppliersExamples::helloWorldSupplier, 50, TimeUnit.MILLISECONDS);
        log.info(memoizedExpiring.get());
        log.info(memoizedExpiring.get());
        log.info("sleeping");
        TimeUnit.MILLISECONDS.sleep(100);
        log.info(memoizedExpiring.get());
        log.info(memoizedExpiring.get());
        log.info("sleeping");
        TimeUnit.MILLISECONDS.sleep(100);
        log.info(memoizedExpiring.get());
        log.info(memoizedExpiring.get());
        // {{end:memoizeWithExpiration}}
    }
}
