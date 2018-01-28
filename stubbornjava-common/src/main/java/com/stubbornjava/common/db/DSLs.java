package com.stubbornjava.common.db;

import java.util.function.Supplier;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.Configs;
import com.stubbornjava.common.HealthChecks;
import com.stubbornjava.common.Metrics;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariDataSource;

public class DSLs {
    private static final Logger logger = LoggerFactory.getLogger(DSLs.class);
    private static final Config conf = Configs.properties();

    private DSLs() {}

    // Letting HikariDataSource leak out on purpose here. It won't go very far.
    private enum Transactional {
        INSTANCE(ConnectionPool.getDataSourceFromConfig(conf.getConfig("pools.transactional"), Metrics.registry(), HealthChecks.getHealthCheckRegistry()));
        private final HikariDataSource dataSource;
        private Transactional(HikariDataSource datasource) {
            this.dataSource = datasource;
        }
        public HikariDataSource getDataSource() {
            return dataSource;
        }
    }
    private static HikariDataSource getTransactionalDataSource() {
        return Transactional.INSTANCE.getDataSource();
    }

    private enum Processing {
        INSTANCE(ConnectionPool.getDataSourceFromConfig(conf.getConfig("pools.processing"), Metrics.registry(), HealthChecks.getHealthCheckRegistry()));
        private final HikariDataSource dataSource;
        private Processing(HikariDataSource datasource) {
            this.dataSource = datasource;
        }
        public HikariDataSource getDataSource() {
            return dataSource;
        }
    }

    private static HikariDataSource getProcessingDataSource() {
        return Processing.INSTANCE.getDataSource();
    }

    public static DSLContext any() {
        return ThreadLocalJooqConfig.getCurrentContext();
    }

    public static DSLContextWrapper transactional() {
        return new DSLContextWrapper(Transactional.INSTANCE.getDataSource());
    }

    public static DSLContextWrapper processing() {
        return new DSLContextWrapper(Processing.INSTANCE.getDataSource());
    }

    public static final class DSLContextWrapper {
        private final HikariDataSource ds;

        public DSLContextWrapper(HikariDataSource ds) {
            this.ds = ds;
        }

        public final DSLContext get() {
            return ThreadLocalJooqConfig.ensureNamedConfiguration(ds.getPoolName());
        }

        public final void newTransaction(Runnable runnable) {
            ThreadLocalJooqConfig.threadLocalTransaction(ds.getPoolName(), ds, runnable);
        }

        public final <T> T newTransactionResult(Supplier<T> supplier) {
            return ThreadLocalJooqConfig.threadLocalTransactionResult(ds.getPoolName(), ds, supplier);
        }
    }

    public static void main(String[] args) {
        logger.debug("starting");
        DataSource processing = DSLs.getProcessingDataSource();
        logger.debug("processing started");
        DataSource transactional = DSLs.getTransactionalDataSource();
        logger.debug("transactional started");
        logger.debug("done");
    }
}