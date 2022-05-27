package com.madm.learnroute.javaee;

public class FinalExample {
    int i;//普通变量
    final int j;//fianl变量
    static FinalExample obj;

    public FinalExample() {//构造函数
        i = 1;//写普通域
        j = 2;//写final域
    }
    public static void writer(){//写线程A执行
        obj = new FinalExample();
    }
    public static void reader(){//读线程B执行
        FinalExample object = obj;//读对象引用5555
        int a = object.i;//读普通域
        int j = object.j;//读final域
    }
}
