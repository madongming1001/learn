package com.madm.learnroute.technology.spring.customproxybean;

import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;

/**
 * @author dongming.ma
 * @date 2022/6/30 11:39
 */
public class CustomTargetSource extends AbstractBeanFactoryBasedTargetSource {

    private static final long serialVersionUID = 1231212121L;

    @Override
    public Object getTarget() throws Exception {
        return getBeanFactory().getBean(getTargetBeanName());
    }
}