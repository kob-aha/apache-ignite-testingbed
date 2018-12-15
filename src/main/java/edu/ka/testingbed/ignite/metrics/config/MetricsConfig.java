package edu.ka.testingbed.ignite.metrics.config;

import edu.ka.testingbed.ignite.metrics.PrometheusMetricsExposer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.JCacheMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.commons.compress.utils.Lists;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PrometheusMetricsExposer.class)
public class MetricsConfig {

    @Bean
    public PrometheusMeterRegistry registry() {
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        return prometheusMeterRegistry;
    }
}
