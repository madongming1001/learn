package com.madm.learnroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {
        "com.madm.learnroute"
})
@EnableAspectJAutoProxy
public class LearnRouteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnRouteApiApplication.class, args);
    }

}