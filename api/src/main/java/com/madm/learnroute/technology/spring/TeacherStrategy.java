package com.madm.learnroute.technology.spring;

import com.mdm.pojo.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Order(Integer.MAX_VALUE - 3)
@Component
@Slf4j
public class TeacherStrategy implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private List<Teacher> list = new ArrayList();


    @Override
    public void afterPropertiesSet() throws Exception {
        //全局 singletonObjects 里面只会放一个 myFactoryBean BeanFactoryUtils.isFactoryDereference(name)会判断传过来的是带不带&开头的带就是获取FactoryBean工具类 反之则是getObject()的对象
        MyFactoryBean externalBean = (MyFactoryBean) applicationContext.getBean("&myFactoryBean");
        MyObject internalBean = (MyObject) applicationContext.getBean("myFactoryBean");
        log.info("externalBean 获取 information is ：{}", externalBean);
        log.info("internalBean 获取 information is ：{}", internalBean);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
