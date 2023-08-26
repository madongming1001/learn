package com.madm.learnroute.technology.spring;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 7:02 PM
 */
//@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WhitelistFilter());
        registration.addUrlPatterns("/*");
        registration.setName("whitelistFilter");
        registration.setOrder(1); // 设置过滤器被调用的顺序
        return registration;
    }
}
