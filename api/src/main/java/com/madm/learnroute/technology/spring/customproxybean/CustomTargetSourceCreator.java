package com.madm.learnroute.technology.spring.customproxybean;

import org.springframework.aop.framework.autoproxy.target.AbstractBeanFactoryBasedTargetSourceCreator;
import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author dongming.ma
 * @date 2022/6/30 11:40
 */
public class CustomTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {
    @Override
    protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName) {
        if (getBeanFactory() instanceof ConfigurableListableBeanFactory) {
            if (beanClass.isAssignableFrom(UserServiceImpl.class)) {
                return new CustomTargetSource();
            }
        }
        return null;
    }
}
