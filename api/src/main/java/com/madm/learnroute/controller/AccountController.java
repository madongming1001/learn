package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import com.mdm.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/save")
    public RestResponse save(@RequestBody @Nullable Account... account) {
        accountService.saveForJdbc(ArrayUtil.isNotEmpty(account) ? account[0] : Account.create());
//        accountService.save(ArrayUtil.isNotEmpty(account) ? account[0] : Account.create());
        return RestResponse.OK();
    }

    @GetMapping("/findAccountById")
    public RestResponse findAccountById(@RequestParam int id) {
        return RestResponse.OK().setBody(accountService.findAccountById(id));
    }
}
