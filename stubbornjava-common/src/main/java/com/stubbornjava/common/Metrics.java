package com.stubbornjava.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jvm.CachedThreadStatesGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.logback.InstrumentedAppender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
// {{start:metrics}}
public class Metrics {
    /*
     * Use a concurrent map to cache metrics we generate on the fly.
     * For example we generate status code metrics on the fly.
     */
    private static final Map<String, Metric> metricCache = new ConcurrentHashMap<>();
    private static final MetricRegistry registry;
    static {
        registry = new MetricRegistry();
        registry.register("gc", new GarbageCollectorMetricSet());
        registry.register("threads", new CachedThreadStatesGaugeSet(10, TimeUnit.SECONDS));
        registry.register("memory", new MemoryUsageGaugeSet());

        // Logback metrics
        final LoggerContext factory = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger root = factory.getLogger(Logger.ROOT_LOGGER_NAME);
        final InstrumentedAppender metrics = new InstrumentedAppender(registry);
        metrics.setContext(root.getLoggerContext());
        metrics.start();
        root.addAppender(metrics);

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
// {{end:metrics}}
