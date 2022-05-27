package com.madm.learnroute.spring;

import com.madm.learnroute.pojo.Teacher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeacherStrategy implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private List<Teacher> list = new ArrayList();


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        System.out.println(1 & 3);
//
//        // 数相同返回零
//        System.out.println(1 ^ 1);
//        // 数不同就是相加
//        System.out.println(1 | 2);
//
//        boolean isZero = true;
//        boolean isNotZero = true;
//        if(isZero){
//            // 两个数相等的话用^返回为零
//            System.out.println(1 ^ 1);
//            // 不同就为0
//            System.out.println(1 & 3);
//        }
//        if(isNotZero){
//            // ｜ 的话就是数的本身
//            System.out.println(1 | 1);
//            // & 的话就是数的本身
//            System.out.println(1 & 1);
//            // 数不同就是相加
//            System.out.println(1 | 2);
//            // 数不同就是相加
//            System.out.println(1 ^ 2);
//        }
    }
}
