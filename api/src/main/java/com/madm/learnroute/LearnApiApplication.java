package com.madm.learnroute;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author madongming
 */

@SpringBootApplication
@EnableFeignClients
@EnableJSONDoc
@EnableScheduling
public class LearnApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LearnApiApplication.class, args);
//        while(true){
//            String configValue = applicationContext.getEnvironment().getProperty("config.info");
//            try {
//                TimeUnit.SECONDS.sleep(1L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

}