package edu.ka.testingbed.ignite.spring.config;

import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableIgniteRepositories(basePackageClasses = EmployeeRepository.class)
@ComponentScan(basePackages = "edu.ka.testingbed.ignite.spring.repository")
public abstract class SpringDataConfig extends BaseSpringConfig {
    @Override
    public Ignite igniteInstance(IgniteConfiguration configuration) {
        Ignite ignite = Ignition.start(configuration);
        return ignite;
    }
}
