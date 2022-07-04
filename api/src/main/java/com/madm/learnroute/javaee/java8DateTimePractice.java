package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

/**
 * 什么是GMT（Greenwich Mean Time）格林威治平时（也称格林威治时间）
 * 它规定太阳每天经过位于英国伦敦郊区的皇家格林威治天文台的时间为中午12点。
 * 选择了穿过英国伦敦格林威治天文台子午仪中心的一条经线作为零度参考线，这条线，简称格林威治子午线。
 *
 * 什么是UTC（Coodinated Universal Time）
 * 协调世界时，又称世界统一时间、世界标准时间、国际协调时间。
 *
 * Instant——它代表的是时间戳，注意这里默认的Instant是0时区，比北京少8个时区，例子：2018-10-08T09:50:21.852Z，相当于当天北京时间的17:50:21.852
 * LocalDate——不包含具体时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
 * LocalTime——它代表的是不含日期的时间
 * LocalDateTime——它包含了日期及时间，不过还是没有偏移信息或者说时区。
 * ZonedDateTime——这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的，如：2018-10-08T18:12:38.547+08:00[Asia/Shanghai]。
 *
 * 时区
 * 从格林威治本初子午线起，经度每向东或者向西间隔15°，就划分一个时区，在这个区域内，大家使用同样的标准时间
 *
 * 参考文章：https://www.cnblogs.com/theRhyme/p/9756154.html
 *
 */
public class java8DateTimePractice {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";


    @SneakyThrows
    public static void main(String[] args) {
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusWeeks(1);
        LocalDate nextWeek = now.plusWeeks(1);

        // 某个月有多少天
        LocalDate localDate = LocalDate.of(2018, 2, 22);
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        int dayOfMonth = lastDay.getDayOfMonth();
        System.out.println(dayOfMonth);

        // 比较两个LocalDate相差多少年、月、天
        Period period = Period.between(lastWeek, nextWeek);
        Instant instant = Instant.now();

//        Assert.isTrue(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) == System.currentTimeMillis(),"比较的数是不相等的");
//        System.out.println(System.currentTimeMillis());
////        创建多个线程2
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                System.out.println(sdf.format(new Date()));
//                //线程启动 3
//            }).start();
//        }
//        HashMap<String, Long> timeUnitTable = new HashMap();
//        timeUnitTable.put("s", 1000L);
//        timeUnitTable.put("m", 1000L * 60);
//        timeUnitTable.put("h", 1000L * 60 * 60);
//        timeUnitTable.put("d", 1000L * 60 * 60 * 24);
//
//        String[] levelArray = messageDelayLevel.split(" ");
//        for (int i = 0; i < levelArray.length; i++) {
//            String value = levelArray[i];
//            String ch = value.substring(value.length() - 1);
//            Long tu = timeUnitTable.get(ch);
//
//            long num = Long.parseLong(value.substring(0, value.length() - 1));
//            System.out.println(num);
//        }
//
//        System.out.println(TimeUnit.MINUTES.toMillis(1L));
    }
}
