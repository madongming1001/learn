package com.madm.learnroute;

import lombok.extern.slf4j.Slf4j;
import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

/**
 * @author madongming
 */

@SpringBootApplication
@MapperScan(basePackages = "com.madm.learnroute.mapper")
@EnableFeignClients
@EnableJSONDoc
@EnableScheduling
//@EnableBinding(MySource.class)
@Slf4j
public class LearnApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LearnApiApplication.class, args);
        while (true) {
            String configValue = applicationContext.getEnvironment().getProperty("config.info");
            try {
                log.info("config.info property value ：{}", configValue);
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}