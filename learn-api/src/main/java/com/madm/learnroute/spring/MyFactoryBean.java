package com.madm.learnroute.spring;

import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBean implements FactoryBean {
    @Override
    public MyObject getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

}
