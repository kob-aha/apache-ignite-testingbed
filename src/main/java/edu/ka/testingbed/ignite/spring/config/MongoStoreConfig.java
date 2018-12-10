package edu.ka.testingbed.ignite.spring.config;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import edu.ka.testingbed.ignite.model.EmployeeDTO;
import edu.ka.testingbed.ignite.store.CacheMongoStore;
import org.apache.ignite.configuration.CacheConfiguration;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import javax.cache.configuration.FactoryBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class MongoStoreConfig extends BaseSpringConfig {

    private static final int MONGOD_PORT = 27001;

    @Bean(destroyMethod = "stop")
    protected MongodExecutable mongodExecutable() throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable mongoExe = null;

        IMongodConfig mongoCfg = new MongodConfigBuilder().
                version(Version.Main.PRODUCTION).
                net(new Net(MONGOD_PORT, Network.localhostIsIPv6())).
                build();

        mongoExe = starter.prepare(mongoCfg);
        mongoExe.start();

        return mongoExe;
    }

    @Bean
    @DependsOn("mongodExecutable")
    public MongoClient mongoClient() {
        MongoClient mongoClient = new MongoClient("localhost", MONGOD_PORT);
        return mongoClient;
    }

    @Bean
    public Datastore morphiaDatastore(MongoClient mongoClient) {
        Set<Class> clss = new HashSet<>();
        Collections.addAll(clss, EmployeeDTO.class);

        Datastore datastore = new Morphia(clss).createDatastore(mongoClient, "test");

        return datastore;
    }

    @Bean
    @Override
    public CacheConfiguration employeeCacheConfiguration() {
        CacheConfiguration cache = super.employeeCacheConfiguration();
        cache.setCacheStoreFactory(FactoryBuilder.factoryOf(CacheMongoStore.class));
        cache.setReadThrough(true);
        cache.setWriteThrough(true);

        return cache;
    }
}
