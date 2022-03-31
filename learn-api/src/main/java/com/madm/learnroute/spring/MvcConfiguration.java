package com.madm.learnroute.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:45 PM
 */
//实现接口 WebMvcConfigurer(官方推荐)
//implements WebMvcConfigurer

//继承WebMvcConfigurationSupport
//extends WebMvcConfigurationSupport
@Configuration
public class MvcConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppkeyInterceptor()).addPathPatterns("/*").order(1);
        // 这里可以配置拦截器启用的 path 的顺序，在有多个拦截器存在时，任一拦截器返回 false 都会使后续的请求方法不再执行
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add((HandlerMethodArgumentResolver) new AuthParamResolver());
    }
}
