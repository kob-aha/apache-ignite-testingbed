package edu.ka.testingbed.ignite.app;

import edu.ka.testingbed.ignite.spring.config.SpringDataConfig;
import edu.ka.testingbed.ignite.model.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.CacheEvent;
import org.apache.ignite.events.Event;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class IgniteAppSpringData2 extends IgniteAppBase {


    public IgniteAppSpringData2(EmployeeRepository repository, Ignite ignite) {
        super(repository, ignite);
    }

    @Override
    public void doRunApplication() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(2);
        employeeDTO.setName("John2");
        employeeDTO.setEmployed(false);

        repository.save(employeeDTO.getId(), employeeDTO);

        repository.getEmployeeDTOById(employeeDTO.getId());

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5l));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IgniteCache<Integer, EmployeeDTO> cache = ignite.cache("cache");
        EmployeeDTO employeeDTOFromCache = cache.get(1);

        System.out.println("Reading value entered by 1st node. Value is: " + employeeDTOFromCache);

        IgnitePredicate<? extends Event> igniteListener = getIgniteListener();

        ignite.events().localListen(igniteListener,
                EventType.EVTS_CACHE);
    }

    public IgnitePredicate<? extends Event> getIgniteListener() {
        IgnitePredicate<CacheEvent> eventsListener = evt -> {
            System.out.println("Received event [evt=" + evt.name() + ", key=" + evt.key() +
                    ", oldVal=" + evt.oldValue() + ", newVal=" + evt.newValue());

            return true; // Continue listening.
        };

        return eventsListener;
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
            URL keystoreURL = this.getClass().getClassLoader().getResource("scripts/instance2.jks");
            URL truststoreURL = this.getClass().getClassLoader().getResource("scripts/instancescerts.jks");
            String keystorePath = Paths.get(keystoreURL.toURI()).toString();
            String truststorePath = Paths.get(truststoreURL.toURI()).toString();
            char[] trustStorePwd = System.getenv(STOREPASS_KEY).toCharArray();

            SslContextFactory sslContextFactory = getSslContextFactory(keystorePath, truststorePath, trustStorePwd);

            return sslContextFactory;
        }

        @Bean
        public IgniteAppBase app(EmployeeRepository repository, Ignite ignite) {
            return new IgniteAppSpringData2(repository, ignite);
        }
    }
}