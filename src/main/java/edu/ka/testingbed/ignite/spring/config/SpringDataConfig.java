package edu.ka.testingbed.ignite.spring.config;

import edu.ka.testingbed.ignite.spring.dto.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.apache.ignite.ssl.SslContextFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

import static org.apache.ignite.events.EventType.*;

@Configuration
@EnableIgniteRepositories(basePackageClasses = EmployeeRepository.class)
@ComponentScan(basePackages = "edu.ka.testingbed.ignite.spring.repository")
public abstract class SpringDataConfig {

    @Bean
    public Ignite igniteInstance() throws URISyntaxException {
        IgniteConfiguration config = new IgniteConfiguration();
        config.setIncludeEventTypes(EVTS_CACHE);

        CacheConfiguration cache = getEmployeeCacheConfiguration();
        CacheConfiguration simpleCacheConfiguration = getSimpleCacheConfiguration();
        config.setCacheConfiguration(cache, simpleCacheConfiguration);

        customizeIgniteConfiguration(config);

        return Ignition.start(config);
    }

    @NotNull
    private CacheConfiguration getEmployeeCacheConfiguration() {
        CacheConfiguration cache = new CacheConfiguration("baeldungCache");
        cache.setCacheMode(CacheMode.REPLICATED);

        cache.setIndexedTypes(Integer.class, EmployeeDTO.class);
        return cache;
    }

    private CacheConfiguration getSimpleCacheConfiguration() {
        CacheConfiguration cache = new CacheConfiguration("simpleCache");
        cache.setCacheMode(CacheMode.REPLICATED);

        cache.setIndexedTypes(String.class, String.class);
        return cache;
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
