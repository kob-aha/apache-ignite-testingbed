package edu.ka.testingbed.ignite.metrics;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.JCacheMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.commons.compress.utils.Lists;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@Component
public class PrometheusMetricsExposer {

    @Autowired
    private PrometheusMeterRegistry prometheusRegistry;

    @Autowired
    private Ignite ignite;

    private JCacheMetrics metricsBinder;

    @PostConstruct
    public void exposeMetrics() {
//        initMetricsBinder();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {
                String response = prometheusRegistry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initMetricsBinder() {
        metricsBinder = new JCacheMetrics(ignite.cache("cache"), Lists.newArrayList());
        metricsBinder.bindTo(this.prometheusRegistry);
    }

}
