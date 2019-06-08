package com.usaa.support.cacheutil;

import com.hazelcast.cache.HazelcastCachingProvider;
import com.hazelcast.cache.ICache;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientClasspathXmlConfig;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.serialization.HazelcastSerializationException;
import org.apache.commons.cli.HelpFormatter;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Simple JCache Utility
 *
 */
public class CacheUtil
{

    private Arguments arguments;
    private ILogger logger = null;

    public CacheUtil(Arguments arguments)
    {
        this.arguments = arguments;
    }

    public static void main( String[] args )
    {
        Arguments arguments = Arguments.createArguments(args);

        int execResult = 0;

        CacheUtil util = new CacheUtil(arguments);
        if(Action.usage.equals(arguments.getAction()))
        {
            util.printHelp();
        }
        else
        {
            util.executeWith(arguments);
            HazelcastClient.shutdownAll();
        }

        System.exit(execResult);

    }

    private void printHelp()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java -jar cacheutil.jar", Arguments.createOptions() );
    }

    private int executeWith(Arguments arguments)
    {
        ClientConfig config = new ClientClasspathXmlConfig(arguments.getXmlFile());
        config.setInstanceName( "my-named-hazelcast-instance" );

        HazelcastInstance instance = HazelcastClient.newHazelcastClient( config );
        CachingProvider cachingProvider = Caching.getCachingProvider();
        // Create Properties instance pointing to a named HazelcastInstance
        Properties properties = HazelcastCachingProvider
                .propertiesByInstanceName( "my-named-hazelcast-instance" );

        CacheManager cacheManager = cachingProvider
                .getCacheManager( null, instance.getClass().getClassLoader(), properties );

        log(Level.INFO, "CacheManager:" + cacheManager.toString());

        this.logger = instance.getLoggingService().getLogger(getClass().getName());

        log(Level.INFO, arguments.toString());

        ICache<Object, Object> cache = null;

        int returnResult = 0;

        if(Action.create.equals(arguments.getAction()))
        {
            if(create(cacheManager))
            {
                log(Level.INFO,"Created cache named: " + arguments.getCacheName());
            }
            else
            {
                log(Level.INFO,"Failed to create cache named: " + arguments.getCacheName());
                returnResult = -2;
            }
            return returnResult;


        }

        if(cacheManager.getCache(arguments.getCacheName()) == null)
        {
            log(Level.INFO, "Cache: " + arguments.getCacheName() + " is null.");
            return -1;
        }

        cache = cacheManager.getCache(arguments.getCacheName()).unwrap(ICache.class);

        log(Level.INFO, "Cache [" + arguments.getCacheName() + "] size=" + cache.size());

        switch (arguments.getAction())
        {
            case get: {
                Object result = get(cache, arguments.getKey());
                if (result != null) {
                    log(Level.INFO, "FOUND key [" + arguments.getKey() + "] value: " + result.toString());
                } else {
                    log(Level.INFO,"Invalid key: " + arguments.getKey());
                }
            }
            break;
            case put:
            {
                boolean result = put(cache, arguments.getKey(), arguments.getValue());
                if (result) {
                    log(Level.INFO,arguments.getKey() + " put successful.");
                } else {
                    log(Level.INFO,arguments.getKey() + ": put unsuccessful.");
                }
            }
                break;
            case remove: {
                boolean result = remove(cache, arguments.getKey());
                if (result) {
                    log(Level.INFO,arguments.getKey() + " removed.");
                } else {
                    log(Level.INFO,arguments.getKey() + ": Invalid key.");
                }
            }
            break;
            case destroy: {
                if (destroy(cache)) {
                    log(Level.INFO, "Successfully destroyed cache name: " + arguments.getCacheName());
                } else {
                    log(Level.INFO, "Failed to destroy cache name: " + arguments.getCacheName());
                }
            }
                break;
            case getAll:
                getAll(cache);


        }

        return returnResult;

    }

    private void log(Level level, String message)
    {
        if(logger != null)
        {
            logger.log(level, message);
        }
        else
        {
            System.out.println("" + level.getName() + " message: " + message);
        }
    }

    private boolean create(CacheManager cacheManager)
    {
//        CompleteConfiguration<Object, Object> config =
//                new MutableConfiguration<Object, Object>()
//                        .setTypes( Object.class, Object.class );

        MutableConfiguration<Object, Object> config = new MutableConfiguration<>();
        config.setStoreByValue(true)
                .setTypes(Object.class, Object.class)
                .setStatisticsEnabled(true);

        log(Level.FINE,"About to create cache named: " + arguments.getCacheName());
        Cache<Object, Object> cache = cacheManager.createCache(arguments.getCacheName(), config);

        return cache != null;
    }

    private boolean destroy(ICache<Object, Object> cache)
    {
        assert cache != null;

        log(Level.INFO,"About to destroy cache name: " + cache.getName());
        cache.destroy();
        return cache.isDestroyed();
    }

    private boolean put(Cache<Object, Object> cache, Object key, Object value)
    {
        cache.put(key, value);
        return cache.containsKey(key);
    }

    private Object get(Cache<Object, Object> cache, Object key)
    {
        return cache.get(key);
    }

    private boolean remove(Cache<Object, Object> cache, Object key)
    {
        boolean result = cache.remove(key);
        if(cache.containsKey(key))
        {
            throw new RuntimeException("FAILED to remove key: " + key);
        }
        return result;
    }

    private void getAll(ICache<Object, Object> cache)
    {
        try {

            Iterator<Cache.Entry<Object, Object>> allCacheEntries = cache.iterator();
            while (allCacheEntries.hasNext()) {
                Cache.Entry<Object, Object> currentEntry = allCacheEntries.next();
                log(Level.INFO,"Key: " + currentEntry.getKey() + " Value: " + currentEntry.getValue().toString());
            }
        }
        catch (HazelcastSerializationException e)
        {
            log(Level.SEVERE,"*****************************************************************************");
            log(Level.SEVERE,"Perhaps you should add Cached Object's Domain classes to your ClassPath??????");
            log(Level.SEVERE,"*****************************************************************************");
            e.printStackTrace();

        }
        catch (Exception e)
        {
            log(Level.SEVERE,"*****************************************************************************");
            log(Level.SEVERE,"Unexpected Exception!!!!!");
            log(Level.SEVERE,"*****************************************************************************");
            e.printStackTrace();
        }

    }
}
