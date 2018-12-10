package edu.ka.testingbed.ignite.spring.config;

import edu.ka.testingbed.ignite.model.EmployeeDTO;
import edu.ka.testingbed.ignite.spring.repository.EmployeeRepository;
import edu.ka.testingbed.ignite.store.CacheMongoStore;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;

public abstract class MongoStoreConfig extends BaseSpringConfig {
    @NotNull
    @Override
    protected CacheConfiguration getEmployeeCacheConfiguration() {
        CacheConfiguration cache = super.getEmployeeCacheConfiguration();

        Factory<CacheMongoStore> storeFactory = FactoryBuilder.factoryOf(CacheMongoStore.class);
        cache.setCacheStoreFactory(storeFactory);
        cache.setReadThrough(true);
        cache.setWriteThrough(true);

        return cache;
    }
}
