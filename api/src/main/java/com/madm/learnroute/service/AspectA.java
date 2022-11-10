package com.madm.learnroute.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectA {

    @Before(value = "execution(public void com.madm.learnroute.service.CircularServiceA.methodA(..)) && args(a, b)", argNames = "a,b")
    public void before(String a, String b) {
//        System.out.println(a + ":" + b);
//        System.out.println("Before advice exec ...");
    }

    @Around(value = "execution(public void com.madm.learnroute.service.CircularServiceA.methodA(..))")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("Around advice start exec ...");
        try {
            pjp.proceed();
        } catch (Exception e) {
            System.out.println(e);
        }
//        System.out.println("Around advice end exec ...");
    }

    @After(value = "execution(public void com.madm.learnroute.service.CircularServiceA.methodA(..)) && args(a, b)", argNames = "a,b")
    public void after(String a, String b) {
//        System.out.println("After advice exec ...");
    }

    @AfterThrowing(value = "execution(public void com.madm.learnroute.service.CircularServiceA.methodA(..)) && args(a, b)", argNames = "a,b")
    public void afterThrowing(String a, String b) {
//        System.out.println("AfterThrowing advice exec ...");
    }

    @AfterReturning(value = "execution(public void com.madm.learnroute.service.CircularServiceA.methodA(..)) && args(a, b)", argNames = "a,b")
    public void afterReturning(String a, String b) {
//        System.out.println("AfterReturning advice exec ...");
    }

}
