package com.madm.learnroute.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import com.madm.learnroute.mapper.AccountMapper;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:18
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public int saveForJdbc(Account account) {
        return accountMapper.insert(account);
        //使用的还是DataSourceUtils.getConnection(obtainDataSource());
//        return jdbcTemplate.update("update account set " + account.getUserName() + " = left(" + account.getUserName() + ",9) where id = " + account.getId());
    }

    @Override
    public Account findAccountById(int id) {
        return accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId, id));
    }
}
