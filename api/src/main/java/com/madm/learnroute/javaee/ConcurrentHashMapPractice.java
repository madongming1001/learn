package com.madm.learnroute.javaee;

import com.mdm.utils.PrintUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConcurrentHashMapPractice {
    @SneakyThrows
    public static void main(String[] args) {
        ConcurrentHashMap<String, Boolean> conMap = new ConcurrentHashMap<>(2);
        //返回修改之前的值 不会覆盖 value为null报空指针异常
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
//        System.out.println("put:" + map.putIfAbsent("null putifabsent", null));
//        System.out.println("put:" + map.putIfAbsent("hello", "123"));
        //onlyIfAbsent参数控制 相同key已经存在的情况下覆盖不服该旧值 true表示不覆盖 false表示覆盖
//        System.out.println("put:" + map.putIfAbsent("hello", "12312414"));
        System.out.println(map.get("hello"));
//        System.out.println("put:" + map.putIfAbsent("hello", "456"));
        System.out.println(map.get("hello"));
        //返回修改之后的值 不会覆盖 value为null不会建立映射关系 如果没有值可以根据传入的key进行映射
//        System.out.println("put:" + map.computeIfAbsent("put", (String t) -> map.computeIfAbsent("put", (String i) -> "i")));
        System.out.println(map.get("null compute if absent"));
        System.out.println(map.size());
        System.out.println("put:" + map.computeIfAbsent("test", (v) -> "python"));
        System.out.println(map.get("test"));
        System.out.println("put:" + map.computeIfAbsent("test", (v) -> "javascript"));
        System.out.println(map.get("test"));
        System.out.println("hello 有值加1 没值就设置默认值：" + map.compute("hello", (key, oldVal) -> oldVal == null ? 1 : Integer.parseInt((String) oldVal) + 1));

        //当值不存在的时候返回的是null
        System.out.println(map.computeIfPresent("hello", (key, val) -> (int) val + 12352353));
        //当值存在的时候会覆盖
        System.out.println(map.get("hello"));
        map.getOrDefault("hello", 1);

        Set<Map.Entry<String, Boolean>> entries = conMap.entrySet();
        for (Map.Entry<String, Boolean> entry : entries) {

        }


        HashMap<Integer, String> objectName = new HashMap<>();
        objectName.putIfAbsent(11, "Java265.com");
        objectName.putIfAbsent(88, "Java爱好者");
        objectName.putIfAbsent(100, "Java网站");
        objectName.putIfAbsent(77, "Java265.com-2");

        // 键值不存在
        String v1 = objectName.merge(101, "-我是新添加的value-1", (oldValue, newValue) -> oldValue + "a" + newValue);
        // 键值存在
        String v2 = objectName.merge(100, "-我是新添加的value-2", (oldValue, newValue) -> oldValue + "b" + newValue);
        PrintUtil.printSplitLine();
        System.out.println("添加的返回值:" + v1);
        System.out.println("添加的返回值:" + v2);

        System.out.println("objectName:" + objectName);
        PrintUtil.printSplitLine();


//        System.out.println(5 << 2 == 5 * 2);
//        System.out.println(31 - Integer.numberOfLeadingZeros(29));
        System.out.println(5 << 2);
        //0101 -> 10 0100 -> 1 0100

        calculateArrayElementPosition();
        System.out.println(1 << (16 - 1));
        System.out.println(245275 << 16);
        System.out.println(12 >>> 3);
        System.out.println(64 < 64 == true);
        System.out.println(Integer.numberOfLeadingZeros(16) | (1 << (16 - 1)));

        System.out.println(1L << (31));
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
