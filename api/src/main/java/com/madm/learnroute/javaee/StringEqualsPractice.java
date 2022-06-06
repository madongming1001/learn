package com.madm.learnroute.javaee;


import com.alibaba.fastjson.JSONObject;
import com.mdm.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.openjdk.jol.info.ClassLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly
 */
public class StringEqualsPractice {
    public static void main(String[] args) {
//        String abc = new String("abc");
//        String abc1 = new String("abc");
//        System.out.println(abc == abc1);

        String tags = "{\"name\":\"2\",\"id\":1}";
        Map map = new HashMap();
        if (StringUtils.isNotEmpty(tags)) {
            map = JSONObject.parseObject(tags,HashMap.class);
        }
        System.out.println(map);
//        String str = "null";
//        if(Objects.isNull(str)){
//            System.out.println("结果是null");
//        }
//        System.out.println();
//
//        System.out.println(1 & -1);
//        System.out.println(2 & -2);
//        System.out.println(3 & -3);
//        System.out.println(4 & -4);
//        System.out.println(Integer.toBinaryString(1));
//        System.out.println(Integer.toBinaryString(-1));
//        System.out.println(Integer.toBinaryString(-2));
//        System.out.println(Integer.toBinaryString(-3));

//        System.out.println(abc1 == abc.intern());
    }

    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }
}
