package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import com.mdm.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

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
    public RestResponse save(@RequestBody Account... account) {
        accountService.save(ArrayUtil.isNotEmpty(account) ? account[0] : Account.create());
        return RestResponse.OK();
    }
}
