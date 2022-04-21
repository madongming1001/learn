package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalDateTimePractice {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public static void main(String[] args) {
//        Assert.isTrue(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) == System.currentTimeMillis(),
//                "比较的数是不相等的");

        //创建多个线程2
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(sdf.format(new Date()));
                //线程启动 3
            }).start();
        }
    }
}
