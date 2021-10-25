package com.example.learnroute.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectB {

	@Before("execution(public void com.example.learnroute.service.CircularServiceB.methodB())")
	public void beforeB() {

		System.out.println("beforeB 执行");

	}
}