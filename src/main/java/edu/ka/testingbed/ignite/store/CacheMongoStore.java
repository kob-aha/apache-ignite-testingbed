/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package edu.ka.testingbed.ignite.store;

import com.mongodb.MongoClient;
import edu.ka.testingbed.ignite.model.EmployeeDTO;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.resources.SpringResource;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sample MongoDB embedded cache store.
 *
 * @author @java.author
 * @version @java.version
 */
public class CacheMongoStore extends CacheStoreAdapter<Integer, EmployeeDTO> implements LifecycleAware {

    /** Mongo data store. */
    @SpringResource(resourceClass = Datastore.class)
    private Datastore morphia;

    /** Logger. */
    @LoggerResource
    private IgniteLogger log;

    /** {@inheritDoc} */
    @Override public void start() throws IgniteException {

    }

    /** {@inheritDoc} */
    @Override public void stop() throws IgniteException {
    }

    /** {@inheritDoc} */
    @Override public EmployeeDTO load(Integer key) throws CacheLoaderException {
        EmployeeDTO e = morphia.find(EmployeeDTO.class).field("id").equal(key).get();

        log("Loaded employee: " + e);

        return e;
    }

    /** {@inheritDoc} */
    @Override public void write(Cache.Entry<? extends Integer, ? extends EmployeeDTO> e) throws CacheWriterException {
        morphia.save(e.getValue());

        log("Stored employee: " + e.getValue());
    }

    /** {@inheritDoc} */
    @Override public void delete(Object key) throws CacheWriterException {
        EmployeeDTO e = morphia.find(EmployeeDTO.class).field("id").equal(key).get();

        if (e != null) {
            morphia.delete(e);

            log("Removed employee: " + key);
        }
    }

    /**
     * @param msg Message.
     */
    private void log(String msg) {
        if (log != null) {
            log.info(">>>");
            log.info(">>> " + msg);
            log.info(">>>");
        }
        else {
            System.out.println(">>>");
            System.out.println(">>> " + msg);
            System.out.println(">>>");
        }
    }
}
