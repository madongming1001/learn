package com.madm.learnroute.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madm.learnroute.mapper.AccountMapper;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.service.AccountService;
import com.madm.learnroute.technology.spring.MyApplicationEvent;
import com.mdm.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;
import static com.madm.learnroute.model.Account.createAccount;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:18
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService, SmartInitializingSingleton {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    AccountService accountService;

    @Override
    @Transactional
    public int saveForJdbc(Account account) {
        Object sqlSessionTemplate = SpringUtil.getBean("sqlSessionTemplate");
        Object sqlSessionFactory = SpringUtil.getBean("sqlSessionFactory");
        log.info("sqlSessionTemplate :{}, sqlSessionFactory : {}", sqlSessionTemplate, sqlSessionFactory);
        return baseMapper.insert(createAccount());
//        使用的还是DataSourceUtils.getConnection(obtainDataSource());
//        return jdbcTemplate.update("update account set " + account.getUserName() + " = left(" + account.getUserName() + ",9) where id = " + account.getId());
    }

    @Transactional
    @Override
    public Account findAccountById(int id) {
        super.baseMapper.delete(Wrappers.<Account>lambdaQuery().eq(Account::getId, id));
        return accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId, id));
    }

    @Override
    public List<Account> findAll() {
        IPage<Account> accountPage = accountMapper.pageList(Page.of(10, 10), Wrappers.emptyWrapper());
        return accountPage.getRecords();
    }

    @Override
    public List<Account> findAllForCursor() {
        List<Account> result = Lists.newArrayList();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(); Cursor cursor = sqlSession.getMapper(AccountMapper.class).scan()) {
            cursor.forEach(account -> result.add((Account) account));
        } catch (Exception e) {
            log.error("findAllForCursor method execution exception :{}", ExceptionUtil.getMessage(e));
        }
        return result;
    }

    @Override
    public List<Account> findAllForCursorFetchSize() {
        List<Account> result = Lists.newArrayList();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(); Cursor cursor = sqlSession.getMapper(AccountMapper.class).scanFetchSize()) {
            cursor.forEach(account -> result.add((Account) account));
        } catch (IOException e) {
            log.error("findAllForCursorFetchSize method execution exception :{}", ExceptionUtil.getMessage(e));
        }
        return result;
    }

    @Override
    public void updateAllUserName() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(); AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class)) {
            List<Account> accountList = accountMapper.selectList(Wrappers.emptyWrapper());
            List<String> userNames = RandomUtil.randomChinese2(0, accountList.size());
            for (int i = 0; i < accountList.size(); i++) {
                accountMapper.updateById(new Account().setId(accountList.get(i).getId()).setUserName(userNames.get(i)));
            }
        } catch (Exception e) {
            log.error("updateAllUserName method execution exception :{}", ExceptionUtil.getMessage(e));
        }
    }

    @Override
//    @Transactional
    public void afterSingletonsInstantiated() {
//        accountService.saveForJdbc(createAccount());
        applicationEventPublisher.publishEvent(new MyApplicationEvent(ofPattern(NORM_DATETIME_PATTERN).format(LocalDateTime.now())));
    }
}
