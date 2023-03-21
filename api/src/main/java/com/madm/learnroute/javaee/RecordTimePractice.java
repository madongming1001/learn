package com.madm.learnroute.javaee;


import cn.hutool.core.date.SystemClock;
import org.springframework.util.StopWatch;

/**
 * 记录时间的另一个种方式
 * @author dongming.ma
 * @date 2022/7/3 12:14
 */
public class RecordTimePractice {
    public static void main(String[] args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("A");
        Thread.sleep(500);
        stopWatch.stop();
        stopWatch.start("B");
        Thread.sleep(300);
        stopWatch.stop();
        stopWatch.start("C");
        Thread.sleep(200);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println(SystemClock.now());
    }
}
