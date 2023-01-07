package com.madm.learnroute.technology.spring;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalDateTime;

/**
 * @author dongming.ma
 * @date 2023/3/5 11:24
 */
@Slf4j
public class SpringBootStartupMonitor implements SpringApplicationRunListener {

    public SpringBootStartupMonitor(SpringApplication springApplication, String[] args) {
    }

    @Override
    public void starting() {
        log.info("starting {}", LocalDateTime.now());
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("environmentPrepared {}", LocalDateTime.now());
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("contextPrepared {}", LocalDateTime.now());
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("contextLoaded {}", LocalDateTime.now());
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("started {}", LocalDateTime.now());
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("running {}", LocalDateTime.now());
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("failed {}", LocalDateTime.now());
    }
}
