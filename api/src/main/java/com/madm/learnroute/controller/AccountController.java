package com.madm.learnroute.controller;

import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import com.mdm.model.RestResponse;
import jodd.util.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:34
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping("/save")
    public RestResponse save(@RequestBody @Nullable Account account) {
        accountService.save(Account.create());
        return RestResponse.OK();
    }
}
