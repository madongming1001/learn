package com.madm.learnroute.technology.spring;

/**
 * @author dongming.ma
 * @date 2022/7/12 22:59
 */

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class MyRegister implements SmartInitializingSingleton {

    private ListableBeanFactory beanFactory;

    public MyRegister(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        String[] beanNames = beanFactory.getBeanNamesForType(TeacherStrategy.class);
        for (String beanName : beanNames) {
//            System.out.println(beanName);
        }
    }

}