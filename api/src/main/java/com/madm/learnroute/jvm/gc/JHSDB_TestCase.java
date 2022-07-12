package com.madm.learnroute.jvm.gc;

import org.openjdk.jol.info.ClassLayout;

/**
 * -XX:+UseCompressedOops 允许对象指针压缩。
 * -XX:+UseCompressedClassPointers 允许类指针压缩。
 * <p>
 * staticObj、instanceObj、localObj存放在哪里?
 * -Xmx10m -XX:+UseSerialGC -XX:+UseCompressedOops -XX:+UseCompressedClassPointers
 * 在 64 位平台上，HotSpot 使用了两个压缩优化技术，Compressed Object Pointers (“CompressedOops”) 和 Compressed Class Pointers。
 * 压缩指针，指的是在 64 位的机器上，使用 32 位的指针来访问数据（堆中的对象或 Metaspace 中的元数据）的一种方式。
 * 这样有很多的好处，比如 32 位的指针占用更小的内存，可以更好地使用缓存，在有些平台，还可以使用到更多的寄存器。
 * 当然，在 64 位的机器中，最终还是需要一个 64 位的地址来访问数据的，所以这个 32 位的值是相对于一个基准地址的值。
 *
 * 参考文章：https://javadoop.com/post/metaspace
 */
public class JHSDB_TestCase {
    static class TestMain {
        //方法区如何实现并没有明确规定 所以静态信息放在了class对象里面
        static ObjectHolder staticObj = new ObjectHolder();
        //对象实例存放在堆中
        ObjectHolder instanceObj = new ObjectHolder();

        void foo() {
            ObjectHolder localObj = new ObjectHolder();
            System.out.println("done"); // 这里设一个断点
        }
    }

    private static class ObjectHolder {
    }

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new TestMain()).toPrintable());
        TestMain testMain = new JHSDB_TestCase.TestMain();
        testMain.foo();
    }
}

