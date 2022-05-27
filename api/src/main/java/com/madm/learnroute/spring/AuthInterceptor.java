package com.madm.learnroute.spring;

import com.madm.learnroute.annotation.UserAuthenticate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 验证方法是否添加@UserAuthenticate注解
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getName().equals("restSuccess") && Objects.isNull(method.getAnnotation(UserAuthenticate.class))) {
            return false;
        }
        return true;
    }
}
