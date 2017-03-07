package com.stubbornjava.common;

import com.codahale.metrics.health.HealthCheckRegistry;

// {{start:registry}}
public class HealthChecks {
    private HealthChecks() {}

    private static final HealthCheckRegistry healthCheckRegistry;
    static {
        healthCheckRegistry = new HealthCheckRegistry();
    }

    public static HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }
}
// {{end:registry}}
