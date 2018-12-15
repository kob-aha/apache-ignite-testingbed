package edu.ka.testingbed.ignite.spring.config;

import edu.ka.testingbed.ignite.CacheContainer;
import edu.ka.testingbed.ignite.model.EmployeeDTO;
import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

import static org.apache.ignite.events.EventType.EVTS_CACHE;

@Configuration
@ComponentScan(basePackages = "edu.ka.testingbed.ignite.metrics.config")
public abstract class BaseSpringConfig {

    @Bean
    public abstract Ignite igniteInstance(IgniteConfiguration configuration);

    @Bean
    private IgniteConfiguration igniteConfiguration(CacheConfiguration... cacheConfigurations) throws URISyntaxException {
        IgniteConfiguration config = new IgniteConfiguration();
        config.setIncludeEventTypes(EVTS_CACHE);

        config.setCacheConfiguration(cacheConfigurations);

        customizeIgniteConfiguration(config);

        return config;
    }

    @Bean
    public CacheConfiguration<Integer, EmployeeDTO> employeeCacheConfiguration() {
        CacheConfiguration cache = new CacheConfiguration("cache");
        cache.setCacheMode(CacheMode.REPLICATED);
        cache.setIndexedTypes(Integer.class, EmployeeDTO.class);
        cache.setStatisticsEnabled(true);

        return cache;
    }

    @Bean
    public CacheContainer cacheContainer(CacheConfiguration<Integer, EmployeeDTO> cacheConfiguration) {
        CacheContainer cacheContainer = new CacheContainer(cacheConfiguration);
        return cacheContainer;
    }

    protected SslContextFactory getSslContextFactory(String keystorePath, String truststorePath, char[] trustStorePwd) {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreFilePath(keystorePath);
        sslContextFactory.setKeyStorePassword(trustStorePwd);
        sslContextFactory.setTrustStoreFilePath(truststorePath);
        sslContextFactory.setTrustStorePassword(trustStorePwd);
        sslContextFactory.setProtocol("TLSv1.2");
        return sslContextFactory;
    }

    protected abstract void customizeIgniteConfiguration(IgniteConfiguration igniteConfiguration) throws URISyntaxException;
}
