package com.madm.learnroute.technology.mybatis.plugin;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * @author dongming.ma
 * @date 2023/3/9 23:27
 */
public class CustomP6SpyLogger implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
//        return StringUtils.isNotBlank(sql) ? " 耗时：" + elapsed + " ms " + now + "\n 执行 SQL：" + sql.replaceAll("[\\s]+", " ") + "\n" : "";
        return StringUtils.isNotBlank(sql) ? " Consume Time：" + elapsed + " ms " + now + "\n Execute SQL：" + sql.replaceAll("[\\s]+", " ") : "";
    }
}
