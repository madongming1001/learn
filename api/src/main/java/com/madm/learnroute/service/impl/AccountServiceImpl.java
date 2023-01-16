package com.madm.learnroute.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import org.apache.commons.lang3.StringUtils;
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
        Object sqlSessionTemplate = SpringUtil.getBean("sqlSessionTemplate");
        Object sqlSessionFactory = SpringUtil.getBean("sqlSessionFactory");
        System.out.println(sqlSessionTemplate + "" + sqlSessionFactory);
        return accountMapper.insert(account);
        //使用的还是DataSourceUtils.getConnection(obtainDataSource());
//        return jdbcTemplate.update("update account set " + account.getUserName() + " = left(" + account.getUserName() + ",9) where id = " + account.getId());
    }

    @Override
    public Account findAccountById(int id) {
        super.baseMapper.delete(Wrappers.<Account>lambdaQuery().eq(Account::getId, id));
        return accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId, id));
    }
}
