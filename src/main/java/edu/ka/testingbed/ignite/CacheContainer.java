package edu.ka.testingbed.ignite;

import edu.ka.testingbed.ignite.model.EmployeeDTO;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

public class CacheContainer {

    private final CacheConfiguration<Integer, EmployeeDTO> cacheConfiguration;
    private CacheManager cacheManager = null;
    private Cache<Integer, EmployeeDTO> employeeCache;

    public CacheContainer(CacheConfiguration<Integer, EmployeeDTO> cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    private void init() {
        if (cacheManager == null) {
            CachingProvider cachingProvider = Caching.getCachingProvider();
            cacheManager = cachingProvider.getCacheManager();

            employeeCache = cacheManager.createCache(cacheConfiguration.getName(), cacheConfiguration);
        }
    }

    public Cache<Integer, EmployeeDTO> getCache() {
        init();

        return employeeCache;
    }
}
