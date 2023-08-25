package com.madm.learnroute.service;

import com.google.common.collect.Maps;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dongming.ma
 * @date 2023/8/24 23:57
 */
public class HandlerFactory {

    private static final ConcurrentMap<String, AbstractHandler> handlers = Maps.newConcurrentMap();


    public static void register(String type, AbstractHandler handler) {
        handlers.put(type, handler);
    }

    public static AbstractHandler getStrategyNoNull(Integer code) {
        AbstractHandler strategy = handlers.get(code);
        Objects.requireNonNull(strategy, () -> "not found strategy class");
        return strategy;
    }
}
