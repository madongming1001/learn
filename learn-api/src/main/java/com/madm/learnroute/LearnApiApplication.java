package com.madm.learnroute;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {"com.madm.learnroute"})
@EnableFeignClients
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableJSONDoc
public class LearnApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnApiApplication.class, args);
    }

}