package com.madm.learnroute.javaee;

import com.mdm.pojo.Teacher;

/**
 *
 */
public class MaxMemoryPractice {

    private String name;
    private Integer age;

    public MaxMemoryPractice() {
    }

    public MaxMemoryPractice(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MaxMemoryPractice[");
        sb.append("name=").append(name);
        sb.append(", age=").append(age);
        return sb.append("]").toString();
    }
}
