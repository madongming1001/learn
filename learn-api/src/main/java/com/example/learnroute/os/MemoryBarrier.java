package com.example.learnroute.os;

/**
 * @author madongming
 */
public class MemoryBarrier {

    int a;
    public volatile int m1 = 1;
    public volatile int m2 = 2;

    public void readAndWrite() {
        int i = m1;//第一个是volatile读
        int j = m2;//第二个是volatile读
        a = i + j;//普通写
        m1 = j + 1;//第一个是volatile写
        m2 = j * 2;//第二个是volatile写
    }

}
