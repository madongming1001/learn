package com.madm.learnroute.technology.disruptor;

import com.madm.learnroute.service.AccountService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取实例化对象
 *
 * @author dongming.ma
 * @date 2023/1/18 17:52
 */
@Component
public class BeanManager implements ApplicationContextAware, InitializingBean {

    private static ApplicationContext applicationContext;

    private final Map<Integer, AccountService> handlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, AccountService> beanMap = applicationContext.getBeansOfType(AccountService.class);
        beanMap.forEach((k, v) -> {
            handlerMap.put(beanMap.get(k).hashCode(), v);
        });
    }

    public AccountService getHandler(Integer type) {
        if (handlerMap.containsKey(type)) {
            return handlerMap.get(type);
        }
        return null;
    }


}
