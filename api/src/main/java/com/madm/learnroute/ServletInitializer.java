package com.madm.learnroute;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 打包成war需要继承此类，还需要修改pom.xml的package form
 *
 * @author dongming.ma
 * @date 2023/2/27 20:02
 */
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LearnApiApplication.class);
    }
}
