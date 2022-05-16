package com.madm.learnroute.spring;

import com.madm.learnroute.annotation.UserAuthenticate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UserAuthenticate annotation = handlerMethod.getMethod().getAnnotation(UserAuthenticate.class);
        if (Objects.isNull(annotation)) {
            return false;
        }
        return true;
    }
}
