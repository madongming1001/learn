package com.madm.learnroute.javaee;

import lombok.SneakyThrows;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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
                try {
                    System.out.println(sdf.parse("2020-9-9 11:11:11"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //线程启动 3
            }).start();
        }
    }
}
