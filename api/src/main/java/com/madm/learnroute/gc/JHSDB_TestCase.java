package com.madm.learnroute.gc;

/**
 * staticObj、instanceObj、localObj存放在哪里?
 * -Xmx10m -XX:+UseSerialGC -XX:-UseCompressedOops
 */
public class JHSDB_TestCase {
    static class Test {
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
        Test test = new JHSDB_TestCase.Test();
        test.foo();
    }
}

