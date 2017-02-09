package com.stubbornjava.common.db;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// {{start:poolFactory}}
public class ConnectionPool {
    private ConnectionPool() {

    }
    /*
     * Expects a config in the following format
     *
     * poolName = "test pool"
     * jdbcUrl = ""
     * maximumPoolSize = 10
     * minimumIdle = 2
     * username = ""
     * password = ""
     * cachePrepStmts = true
     * prepStmtCacheSize = 256
     * prepStmtCacheSqlLimit = 2048
     * useServerPrepStmts = true
     *
     * Let HikariCP bleed out here on purpose
     */
    public static HikariDataSource getDataSourceFromConfig(
        Config conf
        , MetricRegistry metricRegistry
        , HealthCheckRegistry healthCheckRegistry) {

        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setPoolName(conf.getString("poolName"));
        jdbcConfig.setMaximumPoolSize(conf.getInt("maximumPoolSize"));
        jdbcConfig.setMinimumIdle(conf.getInt("minimumIdle"));
        jdbcConfig.setJdbcUrl(conf.getString("jdbcUrl"));
        jdbcConfig.setUsername(conf.getString("username"));
        jdbcConfig.setPassword(conf.getString("password"));

        jdbcConfig.addDataSourceProperty("cachePrepStmts", conf.getBoolean("cachePrepStmts"));
        jdbcConfig.addDataSourceProperty("prepStmtCacheSize", conf.getInt("prepStmtCacheSize"));
        jdbcConfig.addDataSourceProperty("prepStmtCacheSqlLimit", conf.getInt("prepStmtCacheSqlLimit"));
        jdbcConfig.addDataSourceProperty("useServerPrepStmts", conf.getBoolean("useServerPrepStmts"));

        // Add HealthCheck
        jdbcConfig.setHealthCheckRegistry(healthCheckRegistry);

        // Add Metrics
        jdbcConfig.setMetricRegistry(metricRegistry);
        return new HikariDataSource(jdbcConfig);
    }
}
// {{end:poolFactory}}
