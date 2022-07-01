package com.madm.learnroute.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectA {

    @Before("execution(public void com.madm.learnroute.service.CircularServiceA.methodA())")
    public void before() {
        System.out.println("Before advice exec ...");
    }

    @Around("execution(public void com.madm.learnroute.service.CircularServiceA.methodA())")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Around advice start exec ...");
        try {
            pjp.proceed();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Around advice end exec ...");
    }

    @After("execution(public void com.madm.learnroute.service.CircularServiceA.methodA())")
    public void after() {
        System.out.println("After advice exec ...");
    }

    @AfterThrowing("execution(public void com.madm.learnroute.service.CircularServiceA.methodA())")
    public void afterThrowing() {
        System.out.println("AfterThrowing advice exec ...");
    }

    @AfterReturning("execution(public void com.madm.learnroute.service.CircularServiceA.methodA())")
    public void afterReturning() {
        System.out.println("AfterReturning advice exec ...");
    }

}
