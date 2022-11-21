package com.madm.learnroute.technology.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:45 PM
 */
//实现接口 WebMvcConfigurer(官方推荐)
//implements WebMvcConfigurer

//继承 WebMvcConfigurationSupport
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppkeyInterceptor()).addPathPatterns("/**").order(1);
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**").order(2);
        registry.addInterceptor(new TraceLogInterceptor()).addPathPatterns("/**").order(3);
        // 这里可以配置拦截器启用的 path 的顺序，在有多个拦截器存在时，任一拦截器返回 false 都会使后续的请求方法不再执行
    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    //argumentResolvers.add((HandlerMethodArgumentResolver) new AuthParamResolver());
//    }
}
