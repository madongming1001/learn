package com.madm.learnroute.javaee.concurrent.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Fox
 */
@Slf4j
public class ThreadStateTest {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LockSupport.park();
            }
        });
        log.debug("线程状态：{}", thread.getState());
        thread.start();
        log.debug("线程状态：{}", thread.getState());
        Thread.sleep(100);
        log.debug("线程状态：{}", thread.getState());


    }
}
