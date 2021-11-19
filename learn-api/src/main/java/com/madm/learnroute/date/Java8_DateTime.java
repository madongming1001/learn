package com.madm.learnroute.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class Java8_DateTime {

    private static long getTimeMills(){
        LocalDate localDate = LocalDate.now();
        LocalTime localTime =  LocalTime.now();
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), localTime.getHour(), localTime.getMinute(), 0)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    /**
     * 获取明天零点时间戳
     *
     * @return
     */
    private static long getExpireTimeStamp() {
        LocalDate localDate = LocalDate.now();
//        LocalDate localDate = LocalDate.now().plusDays(1);
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static void main(String[] args) {
        System.out.println(getTimeMills());
        System.out.println(getExpireTimeStamp());
    }
}
