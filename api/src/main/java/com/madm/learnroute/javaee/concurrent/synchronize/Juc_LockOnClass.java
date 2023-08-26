package com.madm.learnroute.javaee.concurrent.synchronize;

/**
 * @author ：杨过
 * @date ：Created in 2020/6/28
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
public class Juc_LockOnClass {
    static int stock;

    public static synchronized void decrStock() {
        System.out.println(--stock);
    }

    public static synchronized void cgg() {
        System.out.println();
    }

    public static void main(String[] args) {
        //Juc_LockOnClass.class对象
        Juc_LockOnClass.decrStock();
    }

}
