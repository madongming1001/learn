package com.madm.learnroute.technology.mybatis.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

//@Aspect
//@Component
public class ZhouyuAspect {

//	@DeclareParents(value = "com.zhouyu.service.UserService", defaultImpl = UserImplement.class)
//	private UserInterface userInterface;

    @Before("execution(public void com.zhouyu.service.UserService.test())")
    public void zhouyuBefore(JoinPoint joinPoint) {
        System.out.println("zhouyuBefore");
    }


}
