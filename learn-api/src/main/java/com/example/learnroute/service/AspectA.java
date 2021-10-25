package com.example.learnroute.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectA {

    @Before("execution(public void com.example.learnroute.service.CircularServiceA.methodA())")
    public void beforeA() {
        System.out.println("beforeA 执行");
    }

}
