package com.madm.learnroute.spring;

@FunctionalInterface
public interface ObjectFactory<T> {

    T getObject();
}
