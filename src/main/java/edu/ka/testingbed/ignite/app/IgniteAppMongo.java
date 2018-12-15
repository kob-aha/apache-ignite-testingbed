package edu.ka.testingbed.ignite.app;

import edu.ka.testingbed.ignite.model.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.config.MongoStoreConfig;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.ssl.SslContextFactory;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.cache.Cache;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class IgniteAppMongo extends IgniteAppBase {

    @Autowired
    private Datastore morphiaDatastore;

    public IgniteAppMongo(Ignite ignite) {
        super(null, ignite);
    }

    public void doRunApplication() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1);
        employeeDTO.setName("John");
        employeeDTO.setEmployed(true);

        Cache<Integer, EmployeeDTO> cache = ignite.getOrCreateCache(configuration.getName());
        cache.put(employeeDTO.getId(), employeeDTO);

        Query<EmployeeDTO> employeeDTOQuery = morphiaDatastore.find(EmployeeDTO.class);
        List<EmployeeDTO> employeeDTOs = employeeDTOQuery.asList();

        System.out.println("Read entries from mongo. List is: " + employeeDTOs.toString());
    }

    public static void main (String[] args) {

        IgniteAppBase.runApplication(InstanceConfig.class);
    }

    private static class InstanceConfig extends MongoStoreConfig {

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
        public IgniteAppBase app(Ignite ignite) {
            return new IgniteAppMongo(ignite);
        }
    }
}