package com.madm.learnroute.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "md")
@Configuration
@Data
public class MdConfig {
    private Integer dataCenterId;
    private Integer workerId;
}