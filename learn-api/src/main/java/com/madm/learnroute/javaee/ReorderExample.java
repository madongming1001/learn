package com.madm.learnroute.javaee;

public class ReorderExample {
    int a = 0;
    boolean flag = false;
    public void writer(){
       a = 1;
       flag = true;
    }
    public void reader(){
        if(flag){
            int i = a * a;
            System.out.println(i);
        }
    }
}
