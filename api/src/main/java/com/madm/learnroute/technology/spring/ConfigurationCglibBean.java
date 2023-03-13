package com.madm.learnroute.technology.spring;

import com.mdm.pojo.AuthParam;
import com.mdm.pojo.Invitee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dongming.ma
 * @date 2023/3/13 19:10
 */
@Configuration(proxyBeanMethods = false)
public class ConfigurationCglibBean {

    @Bean
    public Invitee invitee() {
        Invitee invitee = new Invitee();
        invitee.setAuthParam(authParam());
        return invitee;
    }

    @Bean
    public AuthParam authParam() {
        return new AuthParam();
    }
}
