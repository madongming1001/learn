package com.madm.learnroute.concurrency.juc.reentrantlock;

import com.mdm.pojo.User;
import sun.misc.Contended;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {
    static Map<String, Object> map = new HashMap<String, Object>();
    static ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    static Lock readLock = rw.readLock();
    static Lock writeLock = rw.writeLock();
    /**
     * 加运行参数，-XX:-RestrictContended 保证和其他数据不在同一缓存行
     */
    @Contended
    private Long l1 = 0L;

    public static final Object get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public static final Object put(String key, Object value) {
        writeLock.lock();
        try {
            return map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public static final void clear() {
        writeLock.lock();
        try {
            map.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        Cache.put("1", new User());
    }
}
