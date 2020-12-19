package com.stubbornjava.common;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.graphite.GraphiteSender;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

// {{start:sender}}
/**
 * This is a hacked together HTTP sender for grafana cloud.
 * This is NOT the recommended approach to collect metrics.
 * The recommended approach is to use a Carbon-Relay-NG.
 * @author billoneil
 *
 */
class GraphiteHttpSender implements GraphiteSender {
    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GraphiteHttpSender.class);

    private final OkHttpClient client;
    private final String host;
    private final List<GraphiteMetric> metrics = Lists.newArrayList();

    public GraphiteHttpSender(OkHttpClient client, String host, String apiKey) {
        this.client = client.newBuilder()
                            .addInterceptor(HttpClient.getHeaderInterceptor("Authorization", "Bearer " + apiKey))
                            .build();
        this.host = host;
    }

    @Override
    public void connect() throws IllegalStateException, IOException {
        // Just no op here
    }

    @Override
    public void close() throws IOException {
        // no op
    }

    @Override
    public void send(String name, String value, long timestamp) throws IOException {
        metrics.add(new GraphiteMetric(name, 10, Double.parseDouble(value), timestamp));
    }

    @Override
    public void flush() throws IOException {
        Request request = new Request.Builder()
                .url(host + "/metrics")
                .post(RequestBody.Companion.create(Json.serializer().toByteArray(metrics), MediaType.Companion.parse("application/json")))
                .build();
        Retry.retryUntilSuccessfulWithBackoff(() -> client.newCall(request).execute());
        metrics.clear();
    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getFailures() {
        // TODO Auto-generated method stub
        return 0;
    }

    private static final class GraphiteMetric {
        private final String name;
        private final int interval;
        private final double value;
        private final long time;

        public GraphiteMetric(@JsonProperty("name") String name,
                              @JsonProperty("interval") int interval,
                              @JsonProperty("value") double value,
                              @JsonProperty("time") long time) {
            this.name = name;
            this.interval = interval;
            this.value = value;
            this.time = time;
        }

        @SuppressWarnings("unused")
		public String getName() {
            return name;
        }
        @SuppressWarnings("unused")
		public int getInterval() {
            return interval;
        }
        @SuppressWarnings("unused")
		public double getValue() {
            return value;
        }
        @SuppressWarnings("unused")
		public long getTime() {
            return time;
        }
    }
}
// {{end:sender}}