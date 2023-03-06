package com.madm.learnroute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.madm.learnroute.model.Account;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.io.Closeable;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:05
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account>, Closeable {
    //分页查询
    @Select("SELECT * FROM account ${ew.customSqlSegment} ")
    IPage<Account> pageList(IPage<Account> page, @Param(Constants.WRAPPER) QueryWrapper<Account> queryWrapper);

    //流式查询
    @Select("SELECT * FROM account")
    Cursor<Account> scan();

    //游标查询
    @Select("SELECT * FROM account")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 5)
    Cursor<Account> scanFetchSize();

}
