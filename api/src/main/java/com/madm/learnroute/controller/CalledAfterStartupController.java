package com.madm.learnroute.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:50
 */
@Component
@AllArgsConstructor
public class CalledAfterStartupController {

    AccountController accountController;
    BookController bookController;
    BookStockController bookStockController;

    @PostConstruct
    public void initializeCall() {
        accountController.save(null);
        bookController.save(null);
        bookStockController.save(null);
    }

}