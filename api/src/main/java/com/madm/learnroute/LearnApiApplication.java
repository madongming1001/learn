package com.madm.learnroute;

import com.madm.learnroute.technology.messagequeue.rocketmq.message.MySource;
import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author madongming
 */

@SpringBootApplication
@EnableFeignClients
@EnableJSONDoc
@EnableScheduling
@EnableBinding(MySource.class)
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