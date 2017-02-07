package com.stubbornjava.common.db;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPoolTest {

    @Test
    public void test() throws SQLException {
        Config config = ConfigFactory.empty()
            .withValue("poolName", ConfigValueFactory.fromAnyRef("test pool"))
            .withValue("jdbcUrl", ConfigValueFactory.fromAnyRef("jdbc:hsqldb:mem:testdb"))
            .withValue("maximumPoolSize", ConfigValueFactory.fromAnyRef(10))
            .withValue("minimumIdle", ConfigValueFactory.fromAnyRef(2))
            .withValue("username", ConfigValueFactory.fromAnyRef("SA"))
            .withValue("password", ConfigValueFactory.fromAnyRef(""))
            .withValue("cachePrepStmts", ConfigValueFactory.fromAnyRef(true))
            .withValue("prepStmtCacheSize", ConfigValueFactory.fromAnyRef(256))
            .withValue("prepStmtCacheSqlLimit", ConfigValueFactory.fromAnyRef(2048))
            .withValue("useServerPrepStmts", ConfigValueFactory.fromAnyRef(true))
        ;
        MetricRegistry metricRegistry = new MetricRegistry();
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        try (HikariDataSource ds = ConnectionPool.getDataSourceFromConfig(config, metricRegistry, healthCheckRegistry)) {
            assertTrue(ds.getPoolName().equals("test pool"));
            assertTrue(ds.getMaximumPoolSize() == 10);
            assertTrue(ds.getMinimumIdle() == 2);
            assertTrue(ds.getUsername().equals("SA"));
            assertTrue(ds.getPassword().equals(""));
            Properties dsp = ds.getDataSourceProperties();
            assertTrue(((boolean)dsp.get("cachePrepStmts")) == true);
            assertTrue(((int)dsp.get("prepStmtCacheSize")) == 256);
            assertTrue(((int)dsp.get("prepStmtCacheSqlLimit")) == 2048);
            assertTrue(((boolean)dsp.get("useServerPrepStmts")) == true);

            // Using identity equals on purpose
            assertTrue(ds.getHealthCheckRegistry() == healthCheckRegistry);
            assertTrue(ds.getMetricRegistry() == metricRegistry);

            try (Connection conn = ds.getConnection()) {
                 assertTrue(conn.isValid(1000));
            }
        }
    }

}
