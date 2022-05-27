package com.madm.learnroute.mybatis;

import com.madm.learnroute.mybatis.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		ClassPathXmlApplicationContext cpxa = new ClassPathXmlApplicationContext();
		context.register(AppConfig.class);
		context.refresh();

		UserService userService = (UserService) context.getBean("userService");
//		userService.test();
	}
}

