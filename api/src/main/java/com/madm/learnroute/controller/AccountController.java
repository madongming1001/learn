package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.controller.param.AccountRequest;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import com.mdm.model.RestResponse;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:34
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    //    @Autowired
    Redisson redisson;

    @GetMapping("/save")
    public RestResponse save(@RequestBody Account... account) {
        accountService.saveForJdbc(ArrayUtil.isNotEmpty(account) ? account[0] : Account.create());
        accountService.save(ArrayUtil.isNotEmpty(account) ? account[0] : Account.create());
        return RestResponse.OK();
    }

    @GetMapping("/findAccountById")
    public Account findAccountById(@RequestBody @Nullable AccountRequest requestParam) {
        RLock rLock = redisson.getLock("accountSearch");
        rLock.lock();
        Account account = accountService.findAccountById(requestParam.getId());
        rLock.unlock();
        return account;
    }

    @GetMapping("/findAll")
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/findAllForCursor")
    public List<Account> findAllForCursor() {
        return accountService.findAllForCursor();
    }

    @GetMapping("/findAllForCursorFetchSize")
    public List<Account> findAllForCursorFetchSize() {
        return accountService.findAllForCursorFetchSize();
    }
}
