package com.stubbornjava.common;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;

import okhttp3.OkHttpClient;

// {{start:reporters}}
class MetricsReporters {

    public static void startReporters(MetricRegistry registry) {
        // Graphite reporter to Grafana Cloud
        OkHttpClient client = new OkHttpClient.Builder()
            //.addNetworkInterceptor(HttpClient.getLoggingInterceptor())
            .build();

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
