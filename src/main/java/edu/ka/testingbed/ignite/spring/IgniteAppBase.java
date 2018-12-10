package edu.ka.testingbed.ignite.spring;

import edu.ka.testingbed.ignite.spring.config.BaseSpringConfig;
import edu.ka.testingbed.ignite.spring.config.SpringDataConfig;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import org.apache.ignite.Ignite;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class IgniteAppBase {

    public static final String STOREPASS_KEY = "storepass";

    protected final EmployeeRepository repository;
    protected final Ignite ignite;

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
        app.doRunApplication();
    }

    protected abstract void doRunApplication();
}