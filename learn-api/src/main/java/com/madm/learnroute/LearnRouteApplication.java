package com.madm.learnroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {
        "com.madm.learnroute"
})
public class LearnRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnRouteApplication.class, args);
    }

}