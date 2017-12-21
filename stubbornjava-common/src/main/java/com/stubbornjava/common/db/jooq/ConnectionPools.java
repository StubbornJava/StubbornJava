package com.stubbornjava.common.db.jooq;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.Configs;
import com.stubbornjava.common.HealthChecks;
import com.stubbornjava.common.Metrics;
import com.stubbornjava.common.db.ConnectionPool;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPools {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPools.class);
    private static final Config conf = Configs.properties();

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

    public static DSLContext connection() {
        return ThreadLocalJooqConfig.getCurrentContext();
    }

    public static void transactionalTransaction(Runnable runnable) {
        HikariDataSource ds = Transactional.INSTANCE.getDataSource();
        ThreadLocalJooqConfig.threadLocalTransaction(ds.getPoolName(), ds, runnable);
    }

    public static void main(String[] args) {
        logger.debug("starting");
        DataSource processing = ConnectionPools.getProcessingDataSource();
        logger.debug("processing started");
        DataSource transactional = ConnectionPools.getTransactionalDataSource();
        logger.debug("transactional started");
        logger.debug("done");
    }
}