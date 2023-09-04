package com.madm.learnroute;

import com.madm.learnroute.technology.spring.MyDeferredImportSelector;
import com.mdm.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author madongming
 */

@SpringBootApplication(scanBasePackages = {"com.madm.learnroute", "com.mdm.pojo"})
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
@EnableConfigurationProperties
public class LearnApiApplication {

    public static void main(String[] args) {
//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");//jdk 1.8
        ConfigurableApplicationContext context = SpringApplication.run(LearnApiApplication.class, args);
//        while (true) {
        String configValue = context.getEnvironment().getProperty("config.info");

        MessageSource messageSource = context.getBean(MessageSource.class);

        String zhMessage = messageSource.getMessage("user.name", null, null, Locale.CHINA);
        String enMessage = messageSource.getMessage("user.name", null, null, Locale.ENGLISH);

        System.out.println("zhMessage = " + zhMessage);

        System.out.println("enMessage = " + enMessage);

        try {
            log.info("启动完成！当前时间是：{}", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
//            log.info("config.info property value ：{}", configValue);
//            log.info("测试jrebel");
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.schedule(() -> context.close(), 10, TimeUnit.SECONDS);

        User user = (User) context.getBean("user");
        System.out.println(user.getName());

//        context.registerShutdownHook();
        /**
         *         MyService myService = (MyService) applicationContext.getBean("myService");
         *         System.out.println(myService.getUserService());
         *         UserService userService = (UserService) applicationContext.getBean("userService");
         *         System.out.println(userService);
         */
//        }
    }

}