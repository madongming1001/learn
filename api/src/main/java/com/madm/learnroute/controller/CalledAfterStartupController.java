package com.madm.learnroute.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:50
 */
@Component
@AllArgsConstructor
@Slf4j

public class CalledAfterStartupController implements DisposableBean {

    AccountController accountController;
    BookController bookController;
    BookStockController bookStockController;
    ScheduleController scheduleController;

    @PostConstruct
    public void initializeCall() {
        accountController.save();
        bookController.save();
        bookStockController.save();
        scheduleController.updateCron();
    }

    @PreDestroy
    public void destroyAnnotation() {
        log.info("注解形式销毁 destroyAnnotation 方法先执行");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("接口形式销毁 destroy 方法后执行");
    }
}