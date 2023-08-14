package com.madm.learnroute.technology.spring;

import com.madm.learnroute.annotation.Whitelist;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:22 PM
 */
@Aspect
public class WhitelistAspect {
    @Before("whitelistPointCut() && @annotation(whitelist)")
    public void checkAppkeyWhitelist(JoinPoint joinPoint, Whitelist whitelist) {
//        checkWhitelist();
        // 可使用 joinPoint.getArgs() 获取Controller方法的参数vv
        // 可以使用 whitelist 变量获取注解参数
    }


    @Pointcut("@annotation(com.madm.learnroute.annotation.Whitelist)")
    public void whitelistPointCut() {
    }
}
