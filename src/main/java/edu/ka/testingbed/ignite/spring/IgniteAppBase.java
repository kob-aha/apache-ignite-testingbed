package edu.ka.testingbed.ignite.spring;

import edu.ka.testingbed.ignite.spring.config.SpringDataConfig;
import edu.ka.testingbed.ignite.spring.dto.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public abstract class IgniteAppBase {

    public static final String STOREPASS_KEY = "storepass";

    protected final EmployeeRepository repository;
    protected final Ignite ignite;

    public IgniteAppBase(EmployeeRepository repository, Ignite ignite) {
        this.ignite = ignite;
        this.repository = repository;
    }

    public static void runApplication(Class<? extends SpringDataConfig> configClass) {

        if (System.getenv().getOrDefault(STOREPASS_KEY, "NONE").equals("NONE")) {
            System.err.println("Missing storepass environment variables");
        } else {
            runApplicationFromConfig(configClass);
        }
    }

    private static void runApplicationFromConfig(Class<? extends SpringDataConfig> configClass) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(configClass);
        context.refresh();
        IgniteAppBase app = context.getBean(IgniteAppBase.class);
        app.doRunApplication();
    }

    protected abstract void doRunApplication();
}