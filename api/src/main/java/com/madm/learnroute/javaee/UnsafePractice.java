package com.madm.learnroute.javaee;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafePractice {
    static Unsafe unsafe = null;
    static long stateOffset = 0;
    private volatile long state = 0;
    static {
        try {
            unsafe = UnsafePractice.getUnsafe();
            stateOffset = unsafe.objectFieldOffset(UnsafePractice.class.getDeclaredField("state"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            //
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }


    public static void main(String[] args) {
        UnsafePractice unsafePractice = new UnsafePractice();
        Boolean sucess = unsafe.compareAndSwapObject(unsafePractice, stateOffset, 0, 1);
        System.out.println(sucess);
    }
}
