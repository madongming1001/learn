package com.madm.learnroute.javaee;

import java.lang.reflect.Constructor;

/**
 * 创建对象的 6 种方法如下
 * <p>
 * 1、使用 new 关键字
 * 2、反射机制
 * 3、Class.newInstance()
 * 4、Constructor.newInstance()
 * 5、实现 Cloneable 接口，使用 clone 方法创建对象
 * 6、序列化和反序列化
 * <p>
 * JVM 对使用 new 方法创建对象的方式进行了优化，默认情况下，new 的效率更高。
 * new 方式创建对象时，会调用类的构造函数。若构造函数中有耗时操作，则会影响 new 方法创建对象的效率。
 * clone 方式创建对象，并不会调用类的构造函数。
 * <p>
 * 参考文章：https://www.jianshu.com/p/18d329ec4c80
 */
public class CreationObjectPractice {

    private static final int COUNT = 10000 * 1000;

    /**
     * new 和 clone 的方式效率对比
     *
     * @param args
     * @throws CloneNotSupportedException
     */
    public static void main(String[] args) throws Exception {
        Class<?> classz = Class.forName("com.madm.learnroute.javaee.MaxMemoryPractice");
        //没有无参构造器默认创建一个
//        MaxMemoryPractice mmp = (MaxMemoryPractice)classz.newInstance();
//        MaxMemoryPractice mmp = MaxMemoryPractice.class.newInstance();
        // 包括public的和非public的，当然也包括private的
        Constructor<?>[] declaredConstructors = MaxMemoryPractice.class.getDeclaredConstructors();
        // 只返回public的~~~~~~(返回结果是上面的子集)
        Constructor<?>[] constructors = MaxMemoryPractice.class.getConstructors();

        Constructor<?> noArgsConstructor = declaredConstructors[0];
        Constructor<?> haveArgsConstructor = declaredConstructors[1];

        //noArgsConstructor.setAccessible(true); // 非public的构造必须设置true才能用于创建实例
        Object mmp1 = noArgsConstructor.newInstance();
        Object mmp2 = haveArgsConstructor.newInstance("fsx", 18);

        System.out.println(mmp1);
        System.out.println(mmp2);


    }

    private static void compareOfCloneAndNewToCreateObject() throws CloneNotSupportedException {
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            Bean bean = new Bean("ylWang");
        }

        long s2 = System.currentTimeMillis();
        Bean bean = new Bean("ylWang");
        for (int i = 0; i < COUNT; i++) {
            Bean b = bean.clone();
        }

        long s3 = System.currentTimeMillis();

        System.out.println("new  = " + (s2 - s1));
        System.out.println("clone = " + (s3 - s2));
    }

}

class Bean implements Cloneable {
    private String name;

    public Bean(String name) {
        this.name = name;
    }

    @Override
    protected Bean clone() throws CloneNotSupportedException {
        return (Bean) super.clone();
    }
}