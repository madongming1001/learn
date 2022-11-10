package com.madm.learnroute.technology.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class MyFactoryBean implements FactoryBean {
    @Override
    public MyObject getObject() throws Exception {
        return new MyObject();
    }

    @Override
    public Class<?> getObjectType() {
        return MyObject.class;
    }

}
