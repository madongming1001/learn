package com.madm.learnroute.javaee;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConcurrentHashMapPractice {
    @SneakyThrows
    public static void main(String[] args) {
        ConcurrentHashMap<String, Boolean> conMap = new ConcurrentHashMap<>(2);
//        System.out.println(5 << 2 == 5 * 2);
//        System.out.println(31 - Integer.numberOfLeadingZeros(29));
        System.out.println(5 << 2);
        calculateArrayElementPosition();
        System.out.println(1 << (16 - 1));
        System.out.println(245275 << 16);
        System.out.println(12 >>> 3);
        System.out.println(64 < 64 == true);
        System.out.println(Integer.numberOfLeadingZeros(16) | (1 << (16 - 1)));
    }

    private static void calculateArrayElementPosition() throws NoSuchFieldException, IllegalAccessException {
        //寻址公式
        //a[i]_address = base_address + i * data_type_size
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe U = (Unsafe) f.get(null);
        Class<String[]> ak = String[].class;
        int base = U.arrayBaseOffset(ak);
        log.info("base:{}", base); // 16，即起始是16
        int scale = U.arrayIndexScale(ak);
        log.info("scale:{}", scale); // 4，即偏移量是 4
        int shift = 31 - Integer.numberOfLeadingZeros(scale);
        log.info("shift:{}", shift); // 2
        for (int i = 0; i < 5; i++) {
            long result = ((long) i << shift) + base; // i 扩大2倍，加上 base
            log.info("result:{}", result);
        }
    }
}
