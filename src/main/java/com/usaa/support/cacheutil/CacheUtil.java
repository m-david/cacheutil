package com.usaa.support.cacheutil;

import com.hazelcast.cache.ICache;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.cache.impl.HazelcastClientCachingProvider;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.Iterator;

/**
 * Simple JCache Utility
 *
 */
public class CacheUtil
{
    public static void main( String[] args )
    {
        Arguments arguments = Arguments.createArguments(args);

        System.out.println(arguments.toString());

        ClientConfig clientConfig = ConfigUtility.createClientConfig(arguments);
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        CachingProvider cachingProvider = 
                Caching.getCachingProvider(HazelcastClientCachingProvider.class.getName());
                //Caching.getCachingProvider();

        CacheManager cacheManager = cachingProvider.getCacheManager();

        // Create and get the cache.
        ICache<String, Object> cache = (ICache) cacheManager.getCache(arguments.getCacheName());
//        client.getDistributedObject("hz:impl:cacheService", arguments.getCacheName());

        System.out.println("Cache [" + arguments.getCacheName() + "] size=" + cache.size());

        switch (arguments.getAction())
        {
            case get: {
                Object result = get(cache, arguments.getKey());
                if (result != null) {
                    System.out.println("FOUND key [" + arguments.getKey() + "] value: " + result.toString());
                } else {
                    System.out.println("Invalid key: " + arguments.getKey());
                }
            }
                break;
            case remove: {
                boolean result = remove(cache, arguments.getKey());
                if (result) {
                    System.out.println(arguments.getKey() + " removed.");
                } else {
                    System.out.println(arguments.getKey() + ": Invalid key.");
                }
            }
                break;
            case getAll:
                getAll(cache);


        }
        client.shutdown();
        System.exit(1);


    }

    private static Object get(Cache<String, Object> cache, String key)
    {
        return cache.get(key);
    }

    private static boolean remove(Cache<String, Object> cache, String key)
    {
        boolean result = cache.remove(key);
        if(cache.containsKey(key))
        {
            throw new RuntimeException("FAILED to remove key: " + key);
        }
        return result;
    }

    private static void getAll(Cache<String, Object> cache)
    {
        Iterator<Cache.Entry<String,Object>> allCacheEntries= cache.iterator();
        while(allCacheEntries.hasNext()){
            Cache.Entry<String,Object> currentEntry = allCacheEntries.next();
            System.out.println("Key: "+currentEntry.getKey()+" Value: "+ currentEntry.getValue().toString());
        }

    }
}
