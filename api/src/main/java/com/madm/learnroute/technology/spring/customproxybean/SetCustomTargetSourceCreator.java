package com.madm.learnroute.technology.spring.customproxybean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 通过 resolveBeforeInstantiation 自定义返回一个代理对象
 *
 * @author dongming.ma
 * @date 2022/6/30 11:40
 */
@Component
@Slf4j
public class SetCustomTargetSourceCreator implements PriorityOrdered, BeanFactoryAware, InitializingBean, DisposableBean, InstantiationAwareBeanPostProcessor {

    private BeanFactory beanFactory;
    private boolean load = false;

    @Override
    public int getOrder() {
        return 45;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (load) {
            AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator = beanFactory.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
            CustomTargetSourceCreator customTargetSourceCreator = new CustomTargetSourceCreator();
            customTargetSourceCreator.setBeanFactory(beanFactory);
            annotationAwareAspectJAutoProxyCreator.setCustomTargetSourceCreators(customTargetSourceCreator);
            load = !load;
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("SetCustomTargetSourceCreator destroy method execution!");
    }
}