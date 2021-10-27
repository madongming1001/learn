package com.example.learnroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {
        "com.example.learnroute"
})
public class LearnRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnRouteApplication.class, args);
    }

}