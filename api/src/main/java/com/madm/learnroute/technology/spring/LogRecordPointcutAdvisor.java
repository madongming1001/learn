package com.madm.learnroute.technology.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * @author dongming.ma
 * @date 2022/6/15 00:49
 */
public class LogRecordPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor implements MethodInterceptor {

    // LogRecord 的解析类 解析有没有@LogRecord注解
//    private LogRecordOperationSource logRecordOperationSource;


    @Override
    public Pointcut getPointcut() {
        return null;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.
                getArguments());
    }

    private Object execute(MethodInvocation invocation, Object target, Method method, Object[] args) {
        return null;
    }
}
