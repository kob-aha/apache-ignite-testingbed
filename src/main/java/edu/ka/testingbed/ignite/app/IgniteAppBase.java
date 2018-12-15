package edu.ka.testingbed.ignite.app;

import edu.ka.testingbed.ignite.CacheContainer;
import edu.ka.testingbed.ignite.model.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.config.BaseSpringConfig;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import io.micrometer.core.instrument.binder.cache.JCacheMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.commons.compress.utils.Lists;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.cache.Cache;

public abstract class IgniteAppBase {

    public static final String STOREPASS_KEY = "storepass";

    protected final EmployeeRepository repository;
    protected final Ignite ignite;

    @Autowired
    protected PrometheusMeterRegistry registry;

    @Autowired
    protected CacheConfiguration<Integer, EmployeeDTO> configuration;

    @Autowired
    protected CacheContainer cacheContainer;

    public IgniteAppBase(EmployeeRepository repository, Ignite ignite) {
        this.ignite = ignite;
        this.repository = repository;
    }

    public static void runApplication(Class<? extends BaseSpringConfig> configClass) {

        if (System.getenv().getOrDefault(STOREPASS_KEY, "NONE").equals("NONE")) {
            System.err.println("Missing storepass environment variables");
        } else {
            runApplicationFromConfig(configClass);
        }
    }

    private static void runApplicationFromConfig(Class<? extends BaseSpringConfig> configClass) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(configClass);
        context.refresh();
        IgniteAppBase app = context.getBean(IgniteAppBase.class);
        app.initMetrics();
        app.doRunApplication();
    }

    protected abstract void doRunApplication();

    protected void initMetrics() {
        Cache<Integer, EmployeeDTO> cache = cacheContainer.getCache();
        JCacheMetrics metricsBinder = new JCacheMetrics(cache, Lists.newArrayList());
        metricsBinder.bindTo(registry);
    }
}