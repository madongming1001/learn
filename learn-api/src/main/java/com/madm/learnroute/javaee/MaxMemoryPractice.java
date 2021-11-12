package com.madm.learnroute.javaee;

import com.madm.pojo.Teacher;

/**
 *
 */
public class MaxMemoryPractice {

    public static final int initData = 666;
    public static Teacher user = new Teacher();

    //    一个方法对应一块栈帧内存区域
    public int compute() {
        // 0 iconst_1 将int类型常量1压入操作数栈
        // 1 istore_1 将int类型值存入局部变量1
        // 2 iconst_2
        // 3 istore_2
        int a = 1;
        int b = 2;
        int c = (a + b) * 10;
        a++;
        return c;
    }

    public static void main(String[] args) {
//        MaxMemoryPractice match = new MaxMemoryPractice();
//        while (true) {
//            match.compute();
//        }
    }
}
