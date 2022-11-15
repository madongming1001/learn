package com.madm.learnroute.javaee;

import cn.hutool.core.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 什么是GMT（Greenwich Mean Time）格林尼治标准时间（也称格林威治时间）
 * 传统的中文译法是「格林威治」，是「Greenwich」的形譯，與上述的英语读音有较大的出入，因此现代有改译「格林尼治」的趋势。
 * 它规定太阳每天经过位于英国伦敦郊区的皇家格林威治天文台的时间为中午12点。
 * 选择了穿过英国伦敦格林威治天文台子午仪中心的一条经线作为零度参考线，这条线，简称格林尼治子午线。
 * 格林尼治标准时间的正午是指当平太阳横穿格林尼治子午线时（也就是在格林尼治上空最高点时）的时间。
 * 由于地球每天的自转是有些不规则的，而且正在缓慢减速，因此格林尼治平时基于天文观测本身的缺陷，目前已经被原子钟报时的协调世界时（UTC）所取代。
 * <p>
 * 什么是UTC（Coodinated Universal Time）
 * 协调世界时，又称世界统一时间、世界标准时间、国际协调时间。
 * UTC 时间是经过平均太阳时（以格林威治时间GMT为准）、地轴运动修正后的新时标以及以秒为单位的国际原子时所综合精算而成。
 * <p>
 * 全球共分为24个标准时区，相邻时区的时间相差一个小时。
 * 参考文章：https://champyin.com/2020/04/24/%E5%BD%BB%E5%BA%95%E5%BC%84%E6%87%82GMT%E3%80%81UTC%E3%80%81%E6%97%B6%E5%8C%BA%E5%92%8C%E5%A4%8F%E4%BB%A4%E6%97%B6/
 * <p>
 * Instant——它代表的是时间戳，注意这里默认的Instant是0时区，比北京少8个时区，例子：2018-10-08T09:50:21.852Z，相当于当天北京时间的17:50:21.852
 * LocalDate——不包含具体时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
 * LocalTime——它代表的是不含日期的时间
 * LocalDateTime——它包含了日期及时间，不过还是没有偏移信息或者说时区。
 * ZonedDateTime——这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的，如：2018-10-08T18:12:38.547+08:00[Asia/Shanghai]。
 * <p>
 * 时区
 * 从格林威治本初子午线起，经度每向东或者向西间隔15°，就划分一个时区，在这个区域内，大家使用同样的标准时间
 * <p>
 * 参考文章：https://www.cnblogs.com/theRhyme/p/9756154.html
 */
@Slf4j
public class java8DateTimePractice {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
    private static Map<String, Long> timeUnitTable;

    static {
        timeUnitTable = new HashMap();
        timeUnitTable.put("s", 1000L);
        timeUnitTable.put("m", 1000L * 60);
        timeUnitTable.put("h", 1000L * 60 * 60);
        timeUnitTable.put("d", 1000L * 60 * 60 * 24);
    }

    @SneakyThrows
    public static void main(String[] args) {
//        testLocalDateAndTime();
        testPeriod();
        testDuration();
//        long ctm = System.currentTimeMillis();
//        long ztm = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
//        long zdt = ZonedDateTime.now().toInstant().toEpochMilli();
//        System.out.println("ctm = " + ctm + " ztm = " + ztm + " zdt = " + zdt);
//        Assert.isTrue(ctm == ztm);


//        System.out.println(LocalDate.now());
//        System.out.println(LocalTime.now());
//        System.out.println(LocalDateTime.now());//中间T代表时间分割的字符
    }

    private static void testLocalDateAndTime() {
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusWeeks(1);
        LocalDate nextWeek = now.plusWeeks(1);
        now.isAfter(nextWeek);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
        System.out.println("==========================================");
        System.out.println(LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")));
        //asia亚洲
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println(timeZone);
        // 某个月有多少天
        LocalDate localDate = LocalDate.of(2018, 2, 22);
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        int dayOfMonth = lastDay.getDayOfMonth();
        System.out.println(dayOfMonth);
        //获取秒数
        Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        //获取毫秒数
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        //时间转字符串格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String dateTime = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        //字符串转时间
        String dateTimeStr = "2018-07-28 14:11:15";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, df);

        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        LocalDateTime ldt = LocalDateTime.parse("2017-09-28 17:07:05",df);
    }

    private static void testDuration() {
//        Duration duration = Duration.ofDays(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        Duration between = Duration.between(LocalDateTime.now(), LocalDateTime.of(2022, 11, 15, 24, 35));
        LocalDateTime before = LocalDateTime.now();
        Instant start = Instant.parse("2017-10-03T10:15:30.00Z");
        Instant end = Instant.parse("2017-10-03T10:16:30.00Z");
        Duration.between(start, end);

    }

    private static void testPeriod() {
        // 比较两个LocalDate相差多少年、月、天
        LocalDate startDate = LocalDate.of(2015, 2, 20);
        LocalDate endDate = LocalDate.of(2017, 1, 15);
        Period period = Period.between(startDate, endDate);
        log.info("Years:" + period.getYears() + " months:" + period.getMonths() + " days:" + period.getDays());
        Assert.isFalse(period.isNegative());
    }

}
