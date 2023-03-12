package com.madm.learnroute.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class CalledAfterStartupController {

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
    public void destroy() {
        log.info("CalledAfterStartupController destroy method execution!");
    }

}