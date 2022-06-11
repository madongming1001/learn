package com.madm.learnroute.technology.mybatis.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author 周瑜
 */
public interface OrderMapper {

	@Select("select 'order'")
	String selectById();
}
