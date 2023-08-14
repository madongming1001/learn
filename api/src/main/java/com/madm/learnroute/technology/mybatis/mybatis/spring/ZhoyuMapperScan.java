package com.madm.learnroute.technology.mybatis.mybatis.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ZhouyuImportBeanDefinitionRegistrar.class)
public @interface ZhoyuMapperScan {
	String value();
}
