package com.madm.learnroute.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.madm.learnroute.model.Account;

import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:07
 */
public interface AccountService extends IService<Account> {

    int saveForJdbc(Account account);

    Account findAccountById(int id);

    List<Account> findAll();

    List<Account> findAllForCursor();

    List<Account> findAllForCursorFetchSize();

    void updateAllUserName();
}