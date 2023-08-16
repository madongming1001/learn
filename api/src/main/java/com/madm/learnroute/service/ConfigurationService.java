package com.madm.learnroute.service;

import com.madm.learnroute.technology.mybatis.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dongming.ma
 * @date 2023/8/15 21:40
 */
@Configuration
//@Component 使用他会导致两个userService的地址不是同一地址 使用不会编译错误 只会建议注入的方式
public class ConfigurationService {
    @Bean
    public MyService myService() {
        MyService myService = new MyService();
        myService.setUserService(userService());
        return myService;
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

}
