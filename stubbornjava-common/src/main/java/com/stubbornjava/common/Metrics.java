package com.stubbornjava.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.util.EC2MetadataUtils;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.CachedThreadStatesGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.logback.InstrumentedAppender;

import ch.qos.logback.classic.LoggerContext;
import okhttp3.OkHttpClient;
// {{start:metrics}}
public class Metrics {
    private static final Logger log = LoggerFactory.getLogger(Metrics.class);

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
        final ch.qos.logback.classic.Logger root = factory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final InstrumentedAppender metrics = new InstrumentedAppender(registry);
        metrics.setContext(root.getLoggerContext());
        metrics.start();
        root.addAppender(metrics);


        // Graphite reporter to Grafana Cloud
        OkHttpClient client = new OkHttpClient.Builder()
            //.addNetworkInterceptor(HttpClient.getLoggingInterceptor())
            .build();

        String graphiteHost = Configs.properties().getString("metrics.graphite.host");
        String grafanaApiKey = Configs.properties().getString("metrics.grafana.api_key");
        final GraphiteHttpSender graphite = new GraphiteHttpSender(client, graphiteHost, grafanaApiKey);
        final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                                                          .prefixedWith(metricPrefix("stubbornjava"))
                                                          .convertRatesTo(TimeUnit.MINUTES)
                                                          .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                          .filter(MetricFilter.ALL)
                                                          .build(graphite);
        reporter.start(10, TimeUnit.SECONDS);

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

    private static String metricPrefix(String app) {
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
