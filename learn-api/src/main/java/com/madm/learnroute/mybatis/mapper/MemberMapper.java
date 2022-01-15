package com.madm.learnroute.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author 周瑜
 */
public interface MemberMapper {

	@Select("select 'member'")
	String selectById();
}
