package com.madm.learnroute.jvm.jmm;

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
 * @date ：Created in 2020/4/29 13:58
 * @version: V1.0
 * @slogan: 不待扬鞭自奋蹄
 * @description:
 **/
public class Jmm01_HappensBefore {
    public static volatile int r = 3;

    public static int g = 6;

    public static volatile double pai = 3.14;

    public static volatile double area;

    public static void caculate() {
        int a = r;
        int b = g;
        area = a * b * pai;
    }

    public static void main(String[] args) {
        caculate();
    }
}
