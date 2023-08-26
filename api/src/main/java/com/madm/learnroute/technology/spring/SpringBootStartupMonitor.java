package com.madm.learnroute.technology.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MS_FORMATTER;

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
        log.info("starting {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("environmentPrepared {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("contextPrepared {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("contextLoaded {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("started {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("running {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("failed {}", NORM_DATETIME_MS_FORMATTER.format(LocalDateTime.now()));
    }
}
