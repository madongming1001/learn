package com.madm.learnroute.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "md")
@Configuration
@Data
@EnableConfigurationProperties
public class MdConfig {
    private Integer dataCenterId;
    private Integer workerId;


    @Bean
    public InjectionApplicationContext injectionApplicationContext(ConfigurableApplicationContext context) {
        return new InjectionApplicationContext(context);
    }
}

@Slf4j
class InjectionApplicationContext implements InitializingBean {

    private ConfigurableApplicationContext context;
    public InjectionApplicationContext(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("InjectionApplicationContext 上下文的状态是：",context.isActive());
    }
}