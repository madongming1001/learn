package com.mdm.springfeature.aspect;

import com.mdm.springfeature.annotation.SwitchSource;
import com.mdm.springfeature.holder.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 17:21
 */
@Aspect
//优先级要设置在事务切面执行之前
@Order(1)
@Component
@Slf4j
public class DataSourceAspect {


    @Pointcut("@annotation(com.mdm.springfeature.annotation.SwitchSource)")
    public void pointcut() {}

    /**
     * 在方法执行之前切换到指定的数据源
     *
     * @param joinPoint
     */
    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        /*因为是对注解进行切面，所以这边无需做过多判定，直接获取注解的值，进行环绕，将数据源设置成远方，然后结束后，清楚当前线程数据源*/
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SwitchSource switchSource = method.getAnnotation(SwitchSource.class);
        log.info("[Switch DataSource]:" + switchSource.value());
        DataSourceHolder.setDataSource(switchSource.value());
    }

    /**
     * 方法执行之后清除掉ThreadLocal中存储的KEY，这样动态数据源会使用默认的数据源
     */
    @After(value = "pointcut()")
    public void after() {
        DataSourceHolder.clearDataSource();
        log.info("[Switch Default DataSource]");
    }
}
