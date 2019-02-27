package com.stubbornjava.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;

import okhttp3.OkHttpClient;

// {{start:reporters}}
class MetricsReporters {
    private static final Logger log = LoggerFactory.getLogger(MetricsReporters.class);

    public static void startReporters(MetricRegistry registry) {
        // Graphite reporter to Grafana Cloud
        OkHttpClient client = new OkHttpClient.Builder()
            //.addNetworkInterceptor(HttpClient.getLoggingInterceptor())
            .build();

        if (!Configs.properties().hasPath("metrics.graphite.host")
            || !Configs.properties().hasPath("metrics.grafana.api_key")) {
            log.info("Missing metrics reporter key or host skipping");
            return;
        }

        String graphiteHost = Configs.properties().getString("metrics.graphite.host");
        String grafanaApiKey = Configs.properties().getString("metrics.grafana.api_key");
        final GraphiteHttpSender graphite = new GraphiteHttpSender(client, graphiteHost, grafanaApiKey);
        final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                                                          .prefixedWith(Metrics.metricPrefix("stubbornjava"))
                                                          .convertRatesTo(TimeUnit.MINUTES)
                                                          .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                          .filter(MetricFilter.ALL)
                                                          .build(graphite);
        reporter.start(10, TimeUnit.SECONDS);
    }
}
// {{end:reporters}}
