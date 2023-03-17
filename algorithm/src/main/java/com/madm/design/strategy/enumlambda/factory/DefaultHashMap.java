package com.madm.design.strategy.enumlambda.factory;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultHashMap<K, V> extends HashMap<K, V> {
    Function<K, V> function;

    public DefaultHashMap(Supplier<V> supplier) {
        this.function = k -> supplier.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        return super.computeIfAbsent((K) key, this.function);
    }
}
