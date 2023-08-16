package com.madm.learnroute.technology.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:22 PM
 */
@Aspect
@Component
public class WhitelistAspect {
    @Before("whitelistPointCut()")
    public void checkAppkeyWhitelist(JoinPoint joinPoint) {
//        checkWhitelist();
        // 可使用 joinPoint.getArgs() 获取Controller方法的参数vv
        // 可以使用 whitelist 变量获取注解参数
    }


    @Pointcut("execution(* com.madm.learnroute.service.CircularServiceA.methodA())")
    public void whitelistPointCut() {
    }
}
