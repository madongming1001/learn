package com.mdm.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class GuavaCacheUtil {

    static Cache<String, String> myMap;

    static {
         myMap = CacheBuilder.newBuilder()
                .expireAfterAccess(30L, TimeUnit.SECONDS)
                .expireAfterWrite(3L, TimeUnit.MINUTES)
                .concurrencyLevel(6)
                .initialCapacity(100)
                .maximumSize(1000)
                .softValues()
                .build();
    }
}
