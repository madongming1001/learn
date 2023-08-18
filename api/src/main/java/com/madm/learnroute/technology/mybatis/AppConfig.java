package com.madm.learnroute.technology.mybatis;


import com.madm.learnroute.technology.mybatis.mybatis.spring.ZhoyuMapperScan;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

@ComponentScan("com.zhouyu")
@ZhoyuMapperScan("com.zhouyu.mapper")
public class AppConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

//	@Bean
//	public SqlSessionFactory sqlSessionFactory() throws Exception {
//		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//		sessionFactoryBean.setDataSource(dataSource());
//		return sessionFactoryBean.getObject();
//	}

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }


    @Bean
    @Order(0)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:4000/tuling?characterEncoding=utf-8&amp;useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("Zhouyu123456***");
        return dataSource;
    }


    @Bean
    public MapperScannerConfigurer configurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.zhouyu.mapper");
        return configurer;
    }


}
