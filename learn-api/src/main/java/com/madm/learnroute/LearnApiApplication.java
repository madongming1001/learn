package com.madm.learnroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {
        "com.madm.learnroute"
})
@EnableAspectJAutoProxy
//@EnableFeignClients
public class LearnApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnApiApplication.class, args);
    }

}