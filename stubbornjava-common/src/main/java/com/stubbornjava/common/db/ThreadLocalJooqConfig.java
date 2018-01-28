package com.stubbornjava.common.db;

import java.util.function.Supplier;

import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ThreadLocalJooqConfig {
    private static final Logger log = LoggerFactory.getLogger(ThreadLocalJooqConfig.class);
    private static final ThreadLocal<NamedConfiguration> configurations = new ThreadLocal<>();

    // This is used to fetch any currently existing configuration.
    public static DSLContext getCurrentContext() {
        return DSL.using(safeGet().getConfiguration());
    }

    // Make sure we have the expected named config.
    // This is useful if we know we are running a long running query
    // and want to ensure we are using a different connection pool than
    // the default pool.
    public static DSLContext ensureNamedConfiguration(String name) {
        NamedConfiguration config = safeGet();
        if (!name.equals(config.getName())) {
            log.error("Expected to find a {} configuration but found {}", name, config.getName());
            throw new IllegalStateException(String.format("Expected to find a %s configuration but found %s", name, config.getName()));
        }
        return DSL.using(config.getConfiguration());
    }

    private static void setNamedConfiguration(String name, Configuration configuration) {
        configurations.set(new NamedConfiguration(name, configuration));
        log.debug("Set ThreadLocal configuration with name {}", name);
    }

    public static void threadLocalTransaction(String name, DataSource ds, Runnable runnable) {
        threadLocalTransactionResult(name, ds, () -> { runnable.run(); return null;} );
    }

    public static <T> T threadLocalTransactionResult(String name, DataSource ds, Supplier<T> supplier) {
        return DSL.using(JooqConfig.defaultConfigFromDataSource(ds))
                  .transactionResult(ctx -> {
                      try {
                          ThreadLocalJooqConfig.setNamedConfiguration(name, ctx);
                          return supplier.get();
                      } finally {
                          configurations.remove();
                          log.debug("Removed ThreadLocal configuration with name {}", name);
                      }
                  });
    }

    private static NamedConfiguration safeGet() {
        NamedConfiguration config = configurations.get();
        Preconditions.checkNotNull(config, "No Configuration has been initialized on this thread.");
        log.debug("Found ThreadLocal configuration with name {}", config.getName());
        return config;
    }
}
