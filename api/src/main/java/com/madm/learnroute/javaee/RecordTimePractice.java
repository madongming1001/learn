package com.madm.learnroute.javaee;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.date.SystemClock;

/**
 * 记录时间的另一个种方式
 * @author dongming.ma
 * @date 2022/7/3 12:14
 */
public class RecordTimePractice {
    public static void main(String[] args) throws Exception {
        StopWatch sw = new StopWatch();
        sw.start("A");
        Thread.sleep(500);
        sw.stop();
        sw.start("B");
        Thread.sleep(300);
        sw.stop();
        sw.start("C");
        Thread.sleep(200);
        sw.stop();
        System.out.println(sw.prettyPrint());
        System.out.println(SystemClock.now());
    }
}
