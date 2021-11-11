package com.madm.learnroute.config;


import com.madm.learnroute.spring.MyBeanFactoryPostProcessor;

public class SpringConfiguration {

    public MyBeanFactoryPostProcessor myBeanFactoryPostProcessor(){
        return new MyBeanFactoryPostProcessor();
    }
}
