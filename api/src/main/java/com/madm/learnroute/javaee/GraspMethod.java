package com.madm.learnroute.javaee;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class GraspMethod {
    static class GrandFather {
        void thinking() throws Throwable {
            System.out.println("i am grandfather");
        }
    }

    static class Father extends GrandFather {
        void thinking() throws Throwable {
            System.out.println("i am father");
        }
    }

    static class Son extends Father {
        void thinking() throws Throwable {
            super.thinking();
            // 请读者在这里填入适当的代码（不能修改其他地方的代码）
            // 实现调用祖父类的thinking()方法，打印"i am grandfather"
//            System.out.println("i am son");
            try {
                MethodType mt = MethodType.methodType(void.class);
                Field lookupImpl = null;
                lookupImpl = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                lookupImpl.setAccessible(true);
                MethodHandle mh = ((MethodHandles.Lookup) lookupImpl.get(null)).findSpecial(GrandFather.class, "thinking", mt, GrandFather.class);
                mh.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        new Son().thinking();
        System.out.println(GraspMethod.class.getClassLoader().loadClass("com.madm.learnroute.socket.NioSelectorServer"));
    }
}
