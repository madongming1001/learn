package com.mdm.springfeature.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.mdm.springfeature.annotation.SwitchSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 17:05
 */
@Configuration
public class MultipleDataSourceConfig {

    /**
     * @Bean：向IOC容器中注入一个Bean
     * @ConfigurationProperties：使得配置文件中以spring.datasource.badguy为前缀的属性映射到Bean的属性中
     */
    @Bean(name = SwitchSource.DEFAULT_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.druid.badguy")
    public DataSource badguyDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 向IOC容器中注入另外一个数据源
     * 全局配置文件中前缀是spring.datasource.goodguy
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.goodguy")
    public DataSource goodguyDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource badguyDataSource, DataSource goodguyDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(5);
        targetDataSources.put("badguyDataSource", badguyDataSource);
        targetDataSources.put("goodguyDataSource", goodguyDataSource);
        return new DynamicDataSource(badguyDataSource, targetDataSources);
    }


}
