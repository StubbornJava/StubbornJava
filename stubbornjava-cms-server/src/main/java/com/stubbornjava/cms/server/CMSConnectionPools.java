package com.stubbornjava.cms.server;

import org.jooq.Configuration;

import com.stubbornjava.common.Configs;
import com.stubbornjava.common.HealthChecks;
import com.stubbornjava.common.Metrics;
import com.stubbornjava.common.db.ConnectionPool;
import com.stubbornjava.common.db.JooqConfig;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariDataSource;

public class CMSConnectionPools {
    private static final Config conf = Configs.properties().getConfig("cms");

    private CMSConnectionPools() {}

    static Configuration transactionalConfig() {
        return JooqConfig.defaultConfigFromDataSource(CMSConnectionPools.transactional());
    }

    static HikariDataSource transactional() {
        return Transactional.INSTANCE.getDataSource();
    }

    static Configuration processingConfig() {
        return JooqConfig.defaultConfigFromDataSource(CMSConnectionPools.processing());
    }

    static HikariDataSource processing() {
        return Processing.INSTANCE.getDataSource();
    }

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
}
