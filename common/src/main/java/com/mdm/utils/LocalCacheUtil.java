package com.mdm.utils;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class LocalCacheUtil {

//    static Cache<String, String> myMap;
    static Cache<Object, Object> graphs;

    static {
//         myMap = CacheBuilder.newBuilder()
//                .expireAfterAccess(30L, TimeUnit.SECONDS)
//                .expireAfterWrite(3L, TimeUnit.MINUTES)
//                .concurrencyLevel(6)
//                .initialCapacity(100)
//                .maximumSize(1000)
//                .softValues()
//                .build();

        graphs = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .refreshAfterWrite(Duration.ofMinutes(1))
                .build();
    }
}
