package com.madm.learnroute.technology.spring;

import org.springframework.stereotype.Component;

/**
 * @author dongming.ma
 * @date 2022/7/1 18:01
 */
@Component
public class MyProxyFactory {

//    @Lazy
//    CircularServiceA circularServiceA;
//
//    @Bean
//    public ProxyFactoryBean proxyFactoryBean() {
//        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
//        return proxyFactoryBean;
//    }
//
//    @Bean
//    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
//        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
//        beanNameAutoProxyCreator.setBeanNames("");
//        beanNameAutoProxyCreator.setInterceptorNames("");
//        beanNameAutoProxyCreator.setProxyTargetClass(true);
//        return beanNameAutoProxyCreator;
//    }
//
//    @PostConstruct
//    public void init() {
//        ProxyFactory proxyFactory = new ProxyFactory();
//    }
//
//    @Bean
//    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.addMethodName("");
//
//        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
//        defaultPointcutAdvisor.setPointcut(pointcut);
//        defaultPointcutAdvisor.setAdvice(null);
//
//        return defaultPointcutAdvisor;
//    }
//
//    @Bean
//    //只会去找spring中的对应的advice的bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//
//        return defaultAdvisorAutoProxyCreator;
//    }

}
