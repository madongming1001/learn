package com.madm.learnroute.jvm.jmm.dcl;

/**
 * @author dongming.ma
 * @date 2022/11/19 12:55
 */
public class EnumSingleton {
    //对外部提供的获取单例的方法
    public static EnumSingleton getInstance() {
        //获取单例对象，返回
        return SingletonEnum.INSTANCE.getSingletonObj();
    }

    //测试
    public static void main(String[] args) {
        EnumSingleton a = EnumSingleton.getInstance();
        EnumSingleton b = EnumSingleton.getInstance();
        System.out.println(a == b);//true
        assert a == b;
    }

    //内部类使用枚举
    private enum SingletonEnum {
        INSTANCE;

        private EnumSingleton singletonObj;

        //在枚举类的构造器里初始化EnumSingleton
        SingletonEnum() {
            singletonObj = new EnumSingleton();
        }

        private EnumSingleton getSingletonObj() {
            return singletonObj;
        }
    }
}
