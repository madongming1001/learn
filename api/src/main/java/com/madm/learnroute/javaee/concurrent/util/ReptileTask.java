package com.madm.learnroute.javaee.concurrent.util;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2023/3/9 10:39
 */
public class ReptileTask implements Runnable {

    private final Phaser phaser;

    public ReptileTask(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try {
            //模拟爬虫时长
            TimeUnit.SECONDS.sleep(3L);
            System.out.println(Thread.currentThread().getName() + " 已爬完～");
            //等待其他参与者
            phaser.arriveAndAwaitAdvance();//到达屏障后，需等待其他参与者，并返回阶段编号。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
