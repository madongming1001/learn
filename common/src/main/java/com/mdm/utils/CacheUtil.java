package com.mdm.utils;


import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * w-tinylfu
 * TinyLFU维护了近期访问记录的频率信息，作为一个过滤器，当新记录来时，只有满足TinyLFU要求的记录才可以被插入缓存。如前所述，作为现代的缓存，它需要解决两个挑战：
 * 一个是如何避免维护频率信息的高开销；
 * 另一个是如何反应随时间变化的访问模式。
 * 首先来看前者，TinyLFU借助了数据流Sketching技术，Count-Min Sketch显然是解决这个问题的有效手段，
 * 它可以用小得多的空间存放频率信息，而保证很低的False Positive Rate。但考虑到第二个问题，就要复杂许多了，
 * 因为我们知道，任何Sketching数据结构如果要反应时间变化都是一件困难的事情，在Bloom Filter方面，
 * 我们可以有Timing Bloom Filter，但对于CMSketch来说，如何做到Timing CMSketch就不那么容易了。
 * TinyLFU采用了一种基于滑动窗口的时间衰减设计机制，借助于一种简易的reset操作：每次添加一条记录到Sketch的时候，
 * 都会给一个计数器上加1，当计数器达到一个尺寸W的时候，把所有记录的Sketch数值都除以2，该reset操作可以起到衰减的作用 。
 * W-TinyLFU主要用来解决一些稀疏的突发访问元素。在一些数目很少但突发访问量很大的场景下，
 * TinyLFU将无法保存这类元素，因为它们无法在给定时间内积累到足够高的频率。因此W-TinyLFU就是结合LFU和LRU，前者用来应对大多数场景，而LRU用来处理突发流量。
 * 在处理频率记录的方案中，你可能会想到用hashMap去存储，每一个key对应一个频率值。那如果数据量特别大的时候，
 * 是不是这个hashMap也会特别大呢。由此可以联想到 Bloom Filter，对于每个key，用n个byte每个存储一个标志用来判断key是否在集合中。原理就是使用k个hash函数来将key散列成一个整数。
 */
public class CacheUtil {

    //    static Cache<String, String> myMap;
    static Cache<Object, Object> cache;

    static {
//         myMap = CacheBuilder.newBuilder()
//                .expireAfterAccess(30L, TimeUnit.SECONDS)
//                .expireAfterWrite(3L, TimeUnit.MINUTES)
//                .concurrencyLevel(6)
//                .initialCapacity(100)
//                .maximumSize(1000)
//                .softValues()
//                .build();

        cache = Caffeine.newBuilder().maximumSize(10_000).expireAfterWrite(Duration.ofMinutes(5)).refreshAfterWrite(Duration.ofMinutes(1)).build();
    }

    /**
     * 手动加载
     *
     * @param key
     * @return
     */
    public Object manulOperator(String key) {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .maximumSize(10)
                .build();
        //如果一个key不存在，那么会进入指定的函数生成value
        Object value = cache.get(key, t -> setValue(key).apply(key));
        cache.put("hello", value);

        //判断是否存在如果不存返回null
        Object ifPresent = cache.getIfPresent(key);
        //移除一个key
        cache.invalidate(key);
        return value;
    }

    /**
     * 同步加载
     *
     * @param key
     * @return
     */
    public Object syncOperator(String key) {
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(k -> setValue(key).apply(key));
        return cache.get(key);
    }

    public Function<String, Object> setValue(String key) {
        return t -> key + "value";
    }

    /**
     * 异步加载
     *
     * @param key
     * @return
     */
    public Object asyncOperator(String key) {
        AsyncLoadingCache<String, Object> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(k -> setAsyncValue(key).get());

        return cache.get(key);
    }

    public CompletableFuture<Object> setAsyncValue(String key) {
        return CompletableFuture.supplyAsync(() -> {
            return key + "value";
        });
    }
    private static void writeToPeripheral(){
    }


}
