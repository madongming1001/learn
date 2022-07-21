package com.madm.learnroute.javaee;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * @author dongming.ma
 * @date 2022/7/6 20:06
 */
public class IDoubleAddPractice {
    public static void main(String[] args) {
//        int i = 1;
        //表达式的返回值是 i++ 之前的值;
//        i++;
//        ++i;
        int  count = 0;
//        for (int i = 0; i < 100; i++) {
//            count++;
//        }
        count = count;


        /**
         *  0 iconst_1
         *  1 istore_1 局部变量表 slot 1 有一个 i = 1
         *  2 iload_1 读取局部变量表 slot 1 到 operand stack
         *  3 iinc 1 by 1 局部变量表 slot 1 加1 变为 2
         *  6 istore_1 把operand stack里面的数据写入到局部变量表 i
         *  7 getstatic #2 <java/lang/System.out : Ljava/io/PrintStream;>
         * 10 iload_1
         * 11 invokevirtual #3 <java/io/PrintStream.println : (I)V>
         * 14 return
         */
//        int i = 1;
        //表达式的返回值是 i++ 之前的值;
//        i = i++;
        //表达式的返回值是 ++i 之后的值;
//        i = ++i;
//        i = ++i + i++;
//        System.out.println(i);

//        ArrayList<String> nums = Lists.newArrayList("1", "2");
//        for (int k = 0; k < nums.size(); k++) {
//            System.out.println(k);
//        }
    }

//    public void nonStaticMain() {
//        double b = 2.0d;
//    }
}
