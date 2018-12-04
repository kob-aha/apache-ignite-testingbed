package edu.ka.testingbed.ignite.spring.config;

import edu.ka.testingbed.ignite.spring.dto.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
@EnableIgniteRepositories(basePackageClasses = EmployeeRepository.class)
@ComponentScan(basePackages = "edu.ka.testingbed.ignite.spring.repository")
public abstract class SpringDataConfig {

    @Bean
    public Ignite igniteInstance() throws URISyntaxException {
        IgniteConfiguration config = new IgniteConfiguration();

        CacheConfiguration cache = getCacheConfiguration();
        config.setCacheConfiguration(cache);

        customizeIgniteConfiguration(config);

        return Ignition.start(config);
    }

    @NotNull
    private CacheConfiguration getCacheConfiguration() {
        CacheConfiguration cache = new CacheConfiguration("baeldungCache");
        cache.setCacheMode(CacheMode.REPLICATED);

        cache.setIndexedTypes(Integer.class, EmployeeDTO.class);
        return cache;
    }

    protected abstract void customizeIgniteConfiguration(IgniteConfiguration igniteConfiguration) throws URISyntaxException;
}
