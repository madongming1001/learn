package com.madm.learnroute.javaee;

import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimePractice {

    public static void main(String[] args) {
//        Assert.isTrue(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) == System.currentTimeMillis(),
//                "比较的数是不相等的");
        System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        System.out.println(System.currentTimeMillis()/1000);
    }
}
