package com.madm.learnroute.technology.spring.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:43 PM
 */
public class AppkeyInterceptor implements HandlerInterceptor {

    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        applicationContext = SpringUtil.getApplicationContext();
        String applicationName = applicationContext.getApplicationName();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 方法在Controller方法执行结束后执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在view视图渲染完成后执行
    }
}
