package com.madm.learnroute.javaee;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.util.UUID;

public class UniqueIDGenerationPractice {
    public static void main(String[] args) {
        //参数1为终端ID
        //参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        System.out.println(Long.toHexString(1512326079740776448L));
        Integer uuid= UUID.randomUUID().toString().replaceAll("-","").hashCode();
        System.out.println(uuid);
        uuid = uuid < 0 ? -uuid : uuid;//String.hashCode() 值会为空
        System.out.println(uuid);
        System.out.println(IdUtil.fastSimpleUUID());
        System.out.println(IdUtil.fastUUID());
        System.out.println(UUID.randomUUID());
    }
}


