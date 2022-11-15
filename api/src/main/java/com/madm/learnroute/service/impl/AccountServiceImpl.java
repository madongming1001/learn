package com.madm.learnroute.service.impl;

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

    @Override
    public int saveForJdbc(Account account) {
        //使用的还是DataSourceUtils.getConnection(obtainDataSource());
        return jdbcTemplate.update("update account set " + account.userName() + " = left(" + account.userName() + ",9) where id = " + account.id());
    }
}
