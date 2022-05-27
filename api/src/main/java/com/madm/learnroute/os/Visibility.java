package com.madm.learnroute.os;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 可见性
 * 原子性
 * 有序性
 * volatile 轻量级锁的机制
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly
 * 查看class文件的汇编指令
 */
@Slf4j
public class Visibility {

    private volatile static boolean initFlag = false;
    private static Integer count = 0;

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            extracted();
            log.info("线程" + Thread.currentThread().getName() + "当前线程嗅探到initFlag的状态的改变");
        }, "线程A");
        threadA.start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread threadB = new Thread(() -> {
            initFlag = true;
            log.info("线程" + Thread.currentThread().getName() + "线程修改了initFlag的值");
        });
        threadB.setName("线程B");
        threadB.start();
//        i++ 读到cpu里面执行
    }

    private static void extracted() {
        count++;
//        while (!initFlag) {}
    }

}
