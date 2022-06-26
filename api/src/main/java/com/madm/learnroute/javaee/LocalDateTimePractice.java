package com.madm.learnroute.javaee;

import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LocalDateTimePractice {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";


    @SneakyThrows
    public static void main(String[] args) {
        Assert.isTrue(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) == System.currentTimeMillis(),
                "比较的数是不相等的");
        System.out.println(System.currentTimeMillis());
//        创建多个线程2
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(sdf.format(new Date()));
                //线程启动 3
            }).start();
        }
        HashMap<String, Long> timeUnitTable = new HashMap();
        timeUnitTable.put("s", 1000L);
        timeUnitTable.put("m", 1000L * 60);
        timeUnitTable.put("h", 1000L * 60 * 60);
        timeUnitTable.put("d", 1000L * 60 * 60 * 24);

        String[] levelArray = messageDelayLevel.split(" ");
        for (int i = 0; i < levelArray.length; i++) {
            String value = levelArray[i];
            String ch = value.substring(value.length() - 1);
            Long tu = timeUnitTable.get(ch);

            long num = Long.parseLong(value.substring(0, value.length() - 1));
            System.out.println(num);
        }

        System.out.println(TimeUnit.MINUTES.toMillis(1L));
    }
}
