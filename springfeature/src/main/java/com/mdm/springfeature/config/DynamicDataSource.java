package com.mdm.springfeature.config;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 17:18
 */

import com.mdm.springfeature.holder.DataSourceHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源，继承AbstractRoutingDataSource
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 返回需要使用的数据源的key，将会按照这个KEY从Map获取对应的数据源（切换）
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //从ThreadLocal中取出KEY
        return DataSourceHolder.getDataSource();
    }

    /**
     * 构造方法填充Map，构建多数据源
     */
    public DynamicDataSource(DataSource dataSource, Map<Object, Object> targetDataSources) {
        //默认的数据源，可以作为主数据源
        super.setDefaultTargetDataSource(dataSource);
        //目标数据源
        super.setTargetDataSources(targetDataSources);
        //执行afterPropertiesSet方法，完成属性的设置
        super.afterPropertiesSet();
    }
}
