package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.controller.param.AccountRequest;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.impl.AccountServiceImpl;
import com.madm.learnroute.validate.ClassNameCondition;
import com.mdm.model.RestResponse;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.List;

import static com.madm.learnroute.model.Account.createAccount;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:34
 */
@RestController
@RequestMapping("/account")
@Conditional(ClassNameCondition.class)
public class AccountController {

    @Resource
    AccountServiceImpl accountService;

    @Resource
    Redisson redisson;

    @GetMapping("/save")
    public RestResponse save(@RequestBody Account... account) {
        accountService.saveForJdbc(ArrayUtil.isNotEmpty(account) ? account[0] : createAccount());
        accountService.save(ArrayUtil.isNotEmpty(account) ? account[0] : createAccount());
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

    @GetMapping("/updateAllUserName")
    public RestResponse updateAllUserName() {
        accountService.updateAllUserName();
        return RestResponse.OK();
    }
}
