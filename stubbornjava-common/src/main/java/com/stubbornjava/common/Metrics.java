package com.stubbornjava.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class Metrics {
    private static final MetricRegistry registry;
    private static final Map<String, Metric> metricCache = new ConcurrentHashMap<>();
    static {
        registry = new MetricRegistry();

        // Register reporters here.
    }

    public static MetricRegistry registry() {
        return registry;
    }

    public static Timer timer(String first, String... keys) {
        String key = MetricRegistry.name(first, keys);
        return (Timer) metricCache.computeIfAbsent(key, (String metricName) -> {
                Timer metric = new Timer();
                registry.register(metricName, metric);
                return metric;
        });
    }

    public static Meter meter(String first, String... keys) {
        String key = MetricRegistry.name(first, keys);
        return (Meter) metricCache.computeIfAbsent(key, (String metricName) -> {
            Meter metric = new Meter();
            registry.register(metricName, metric);
            return metric;
        });
    }
}
