package com.madm.learnroute.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface UserAuthenticate
{
    /**
     * 是否需要校验访问权限 默认不校验
     * 这是预留给咱们后台管理系统的，后台管理员角色不同访问权限不同。所以将这个设置为True后 拦截器做登录校验后，还可以做接口权限校验
     * @return
     */
    boolean permission() default false;

}