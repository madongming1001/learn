package com.mdm.springfeature;

import com.mdm.springfeature.config.MultipleDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MultipleDataSourceConfig.class})
@MapperScan("com.mdm.springfeature.mapper")
public class SpringfeatureApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringfeatureApplication.class, args);
    }
}
