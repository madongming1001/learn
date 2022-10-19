package com.mdm.utils;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LocalCacheUtil {

    static Cache<String, String> myMap;
    static com.github.benmanes.caffeine.cache.Cache<Object, Object> graphs;

    static {
         myMap = CacheBuilder.newBuilder()
                .expireAfterAccess(30L, TimeUnit.SECONDS)
                .expireAfterWrite(3L, TimeUnit.MINUTES)
                .concurrencyLevel(6)
                .initialCapacity(100)
                .maximumSize(1000)
                .softValues()
                .build();

        graphs = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .refreshAfterWrite(Duration.ofMinutes(1))
                .build();
    }
}
