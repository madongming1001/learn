package com.madm.learnroute.javaee.concurrent.util;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2023/3/9 10:42
 */
public class ReduceTask implements Runnable {

    private final Phaser phaser;

    public ReduceTask(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try {
            //模拟爬虫时长
            TimeUnit.SECONDS.sleep(3L);
            System.out.println(Thread.currentThread().getName() + " 收集完毕！");
            //等待其他参与者
            phaser.arrive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}