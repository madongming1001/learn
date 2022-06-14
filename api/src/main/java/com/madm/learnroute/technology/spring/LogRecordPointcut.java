package com.madm.learnroute.technology.spring;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * @author dongming.ma
 * @date 2022/6/14 23:52
 */
public class LogRecordPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return false;
    }
}
