package com.madm.learnroute.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madm.learnroute.mapper.AccountMapper;
import com.madm.learnroute.mapper.BookStockMapper;
import com.madm.learnroute.model.Account;
import com.madm.learnroute.model.BookStock;
import com.madm.learnroute.service.AccountService;
import com.madm.learnroute.service.BookStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:18
 */
@Service
public class BookStockServiceImpl extends ServiceImpl<BookStockMapper, BookStock> implements BookStockService {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public BookStockServiceImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
