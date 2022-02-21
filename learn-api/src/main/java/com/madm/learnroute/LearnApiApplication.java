package com.madm.learnroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {
        "com.madm.learnroute"
})
@EnableFeignClients
@EnableTransactionManagement
public class LearnApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnApiApplication.class, args);
    }

}