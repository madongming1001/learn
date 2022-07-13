package com.madm.learnroute.technology.mybatis;

import com.madm.learnroute.technology.mybatis.service.UserService;
import com.madm.learnroute.technology.spring.MyApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMain {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ClassPathXmlApplicationContext cpxa = new ClassPathXmlApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        context.publishEvent(MyApplicationEvent.class);

        UserService userService = (UserService) context.getBean("userService");
//		userService.test();
    }
}

