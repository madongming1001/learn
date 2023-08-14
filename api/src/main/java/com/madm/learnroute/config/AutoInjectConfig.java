package com.madm.learnroute.config;

import com.madm.learnroute.technology.mybatis.mybatis.spring.ZhoyuMapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author dongming.ma
 * @date 2023/8/14 16:19
 */
@Configuration
@ZhoyuMapperScan("com.madm.learnroute.technology.mybatis.mapper")
public class AutoInjectConfig {
}
