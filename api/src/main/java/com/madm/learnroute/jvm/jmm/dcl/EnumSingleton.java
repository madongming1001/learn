package com.madm.learnroute.jvm.jmm.dcl;

/**
 * 1、枚举类反编译枚举类发现 继承自extend Enum类 并且构造方法标注了说是不可以被程序员调用此构造器，
 * 2、通过一段静态代码块初始化枚举。这段静态代码块的作用就是生成对应的静态常量字段的值，还生成了$VALUES字段，用于保存枚举类定义的枚举常量。
 * 3、values()方法就是克隆之前生成的$VALUES字段之后强转成对应枚举数组
 * 枚举实例的创建也是由编译器完成的。
 *
 * 枚举不能被反射创建枚举，因为在newInstance方法里面就判断了如果是枚举类型就报IllegalArgumentException异常
 * @author dongming.ma
 * @date 2022/11/19 12:55
 */
public class EnumSingleton {
    //对外部提供的获取单例的方法
    public static EnumSingleton getInstanceForEnum() {
        //获取单例对象，返回
        return SingletonEnum.INSTANCE.getSingletonObj();
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
