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
        System.out.println(1 | 1);
        System.out.println(1 ^ 1);
        System.out.println(1 & 1);

        System.out.println(1 | 2);
        System.out.println(1 ^ 2);
        System.out.println(1 & 2);


//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//
//        List<Integer> list1 = new ArrayList<>();
//        list1.add(1);
//        list1.add(7);
//        list1.add(8);
//        list1.add(9);
//        list1.add(10);
//
//        int result = 0;
//        for (int i = 0; i < list.size(); i++) {
//            result = (result ^ list.get(i)) ^ (result ^ list1.get(i));
//        }
//        System.out.println(result);
    }
}
