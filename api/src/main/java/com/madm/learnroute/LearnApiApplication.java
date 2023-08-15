package com.madm.learnroute;

import com.madm.learnroute.service.OrderService;
import com.madm.learnroute.technology.spring.MyDeferredImportSelector;
import lombok.extern.slf4j.Slf4j;
import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author madongming
 */

@SpringBootApplication
@MapperScan(basePackages = "com.madm.learnroute.mapper")
@EnableFeignClients
@EnableJSONDoc
@EnableScheduling
@EnableAsync
//@EnableBinding(MySource.class)
@Slf4j
@EnableCaching
@Import(MyDeferredImportSelector.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class LearnApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LearnApiApplication.class, args);
//        while (true) {
        String configValue = applicationContext.getEnvironment().getProperty("config.info");
        try {
            log.info("启动完成！当前时间是：{}", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
//            log.info("config.info property value ：{}", configValue);
//            log.info("测试jrebel");
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        }
    }

}