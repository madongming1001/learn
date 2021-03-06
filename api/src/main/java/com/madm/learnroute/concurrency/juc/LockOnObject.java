package com.madm.learnroute.concurrency.juc;

import java.util.Objects;

public class LockOnObject {
    private static Object object = new Object();

    private Integer stock = 10;

    public void decrStock(){
        synchronized (object){
            --stock;
            if(stock <= 0){
                System.out.println("库存售罄");
                return;
            }
        }
    }

    public static void main(String[] args) {
        int type = 3;
        switch (type){
            case 1:
                type = 2;
            case 2:
                System.out.println(2222);
            default:
                System.out.println(3333);
        }
        System.out.println("什么方法也没有执行");
        while(true){

        }
    }
}
