package com.madm.learnroute.edu.jmm;

/**
 * ,;,,;
 * ,;;'(    社
 * __      ,;;' ' \   会
 * /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 * ,;' \    /-.,,(   ) \    码
 * ) /       ) / )|    农
 * ||        ||  \)
 * (_\       (_\
 *
 * @author ：杨过
 * @date ：Created in 2020/4/29
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
public class Jmm06_MemoryBarrier {
    int a=0;
    int c=0;
    public volatile int m1 = 1;
    public volatile int m2 = 2;

    public void readAndWrite() {
        int i = m1;   // 第一个volatile读
        a = 4;
        int j = m2;   // 第二个volatile读


        int c = 2; //普通写
        m1 = 3;

        a = i + j;    // 普通写

        m1 = i + 1;   // 第一个volatile写
        m2 = j * 2;   // 第二个 volatile写
    }

}
