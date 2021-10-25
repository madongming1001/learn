package com.example.learnroute.javaee;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class UniqueIDGenerationPractice {
    public static void main(String[] args) {
        //参数1为终端ID
        //参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        // 有两种返回值类型
        Long id = snowflake.nextId();
        for (int i = 0; i <10; i++) {
            String nextIdStr = snowflake.nextIdStr();
            System.out.println(nextIdStr);
        }
    }
}


