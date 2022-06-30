package com.madm.learnroute.technology.spring;

import com.mdm.pojo.Teacher;
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

}
