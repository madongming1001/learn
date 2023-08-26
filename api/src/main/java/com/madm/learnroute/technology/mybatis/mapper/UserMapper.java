package com.madm.learnroute.technology.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author 周瑜
 */
public interface UserMapper {

    @Select("select 'user'")
    String selectById();
}
