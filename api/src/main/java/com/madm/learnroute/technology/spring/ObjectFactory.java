package com.madm.learnroute.technology.spring;

@FunctionalInterface
public interface ObjectFactory<T> {

    T getObject();
}
