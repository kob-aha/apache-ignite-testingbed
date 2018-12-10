package edu.ka.testingbed.ignite.app;

import edu.ka.testingbed.ignite.spring.config.SpringDataConfig;
import edu.ka.testingbed.ignite.model.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class IgniteAppSpringData1 extends IgniteAppBase {

    public IgniteAppSpringData1(EmployeeRepository repository, Ignite ignite) {
        super(repository, ignite);
    }

    public void doRunApplication() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1);
        employeeDTO.setName("John");
        employeeDTO.setEmployed(true);

        repository.save(employeeDTO.getId(), employeeDTO);

        IgniteCache<Integer, EmployeeDTO> cache = ignite.cache("baeldungCache");

        EmployeeDTO cacheReadEntry = cache.get(2);
        System.out.println("Read entry from cache. Value is: " + cacheReadEntry);
    }

    public static void main (String[] args) {

        IgniteAppBase.runApplication(InstanceConfig.class);
    }

    private static class InstanceConfig extends SpringDataConfig {

        @Override
        protected void customizeIgniteConfiguration(IgniteConfiguration igniteConfiguration) throws URISyntaxException {
            igniteConfiguration.setSslContextFactory(getSslContextFactory());
        }

        private SslContextFactory getSslContextFactory() throws URISyntaxException {
            URL keystoreURL = this.getClass().getClassLoader().getResource("scripts/instance1.jks");
            URL truststoreURL = this.getClass().getClassLoader().getResource("scripts/instancescerts.jks");
            String keystorePath = Paths.get(keystoreURL.toURI()).toString();
            String truststorePath = Paths.get(truststoreURL.toURI()).toString();
            char[] trustStorePwd = System.getenv(STOREPASS_KEY).toCharArray();

            SslContextFactory sslContextFactory = getSslContextFactory(keystorePath, truststorePath, trustStorePwd);
            return sslContextFactory;
        }

        @Bean
        public IgniteAppBase app(EmployeeRepository repository, Ignite ignite) {
            return new IgniteAppSpringData1(repository, ignite);
        }
    }
}