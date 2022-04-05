package com.madm.learnroute.javaee;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class UniqueIDGenerationPractice {
    public static void main(String[] args) {
        //参数1为终端ID
        //参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        snowflake.nextId();
    }
}


