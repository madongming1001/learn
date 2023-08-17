package com.madm.learnroute.service;

import com.madm.learnroute.technology.mybatis.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PreDestroy;

/**
 * @author dongming.ma
 * @date 2023/8/15 21:41
 */
@Data
@Slf4j
public class MyService implements DisposableBean {
    private UserService userService;

    public void init() {
        log.info("注解形式销毁 destroyAnnotation 方法先执行");
    }

    public void destroy() {
        log.info("接口形式销毁 destroy 方法后执行");
    }

    public void destroyForBeanWay() {
        log.info("接口形式销毁 destroy 方法后执行");
    }

}
