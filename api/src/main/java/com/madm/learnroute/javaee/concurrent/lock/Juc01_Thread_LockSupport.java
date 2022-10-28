package com.madm.learnroute.javaee.concurrency.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author ：杨过
 * @date ：Created in 2020/4/28
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
@Slf4j
public class Juc01_Thread_LockSupport {

    public static void main(String[] args) {

        Thread t0 = new Thread(new Runnable() {

            @Override
            public void run() {
                Thread current = Thread.currentThread();
                log.info("{},开始执行!",current.getName());
                for(;;){//spin 自旋
                    log.info("准备park住当前线程：{}....",current.getName());
                    log.info("未调用park，当前线程状态：{}",Thread.currentThread().getState());
                    LockSupport.park();
                    log.info("已调用park，当前线程状态：{}",Thread.currentThread().getState());
                    log.info("当前线程{}已经被唤醒....",current.getName());
                }
            }

        },"t0");
        log.info("线程未启动状态是：{}",t0.getState());
        t0.start();

        try {
            Thread.sleep(5000);
            log.info("准备唤醒{}线程!",t0.getName());
            LockSupport.unpark(t0);
            log.info("接触park，当前线程状态：{}",Thread.currentThread().getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
