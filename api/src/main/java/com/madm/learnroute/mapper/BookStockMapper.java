package com.madm.learnroute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.madm.learnroute.model.BookStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:05
 */
@Mapper
public interface BookStockMapper extends BaseMapper<BookStock> {
}
