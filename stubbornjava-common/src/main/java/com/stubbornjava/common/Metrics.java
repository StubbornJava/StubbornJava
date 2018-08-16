package com.stubbornjava.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.util.EC2MetadataUtils;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jvm.CachedThreadStatesGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.logback.InstrumentedAppender;

import ch.qos.logback.classic.LoggerContext;

// {{start:metrics}}
public class Metrics {
    private static final Logger log = LoggerFactory.getLogger(Metrics.class);
    private static final MetricRegistry registry;
    static {
        registry = new MetricRegistry();
        registry.register("gc", new GarbageCollectorMetricSet());
        registry.register("threads", new CachedThreadStatesGaugeSet(10, TimeUnit.SECONDS));
        registry.register("memory", new MemoryUsageGaugeSet());

        // Logback metrics
        final LoggerContext factory = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ch.qos.logback.classic.Logger root = factory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final InstrumentedAppender metrics = new InstrumentedAppender(registry);
        metrics.setContext(root.getLoggerContext());
        metrics.start();
        root.addAppender(metrics);

        // Register reporters here.
        MetricsReporters.startReporters(registry);
    }

    public static MetricRegistry registry() {
        return registry;
    }

    public static Timer timer(String first, String... keys) {
        return registry.timer(MetricRegistry.name(first, keys));
    }

    public static Meter meter(String first, String... keys) {
        return registry.meter(MetricRegistry.name(first, keys));
    }

    static String metricPrefix(String app) {
        Env env = Env.get();
        String host = env == Env.LOCAL ? "localhost" : getHost();
        String prefix = MetricRegistry.name(app, env.getName(), host);
        log.info("Setting Metrics Prefix {}", prefix);
        return prefix;
    }

    private static String getHost() {
        return EC2MetadataUtils.getLocalHostName().split("\\.")[0];
    }
}
// {{end:metrics}}
