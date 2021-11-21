package com.madm.learnroute.spring;

import cn.hutool.core.lang.copier.Copier;
import com.google.common.base.Supplier;
import io.lettuce.core.output.StatusOutput;

public class MyObject {
    public static void getObject(ObjectFactory<?> objectFactory){
        System.out.println(objectFactory.getObject());
    }

    public static void main(String[] args) {
//        String name = "Lisa";
//        getObject(() -> getObjectByName(name));
//
//        Supplier<String> getObject = () -> {
//            return new String("123455");
//        };
        int i = "33333999".hashCode();
        int j = "44444999".hashCode();
        int disturbingTermI = i ^ (i >>> 16);
        int disturbingTermJ = j ^ (j >>> 16);
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toBinaryString(j));
        System.out.println(i + "：hashcode");
        System.out.println(j + "：hashcode");
        System.out.println((disturbingTermI & (i - 1)) + "：扰动函数I");
        System.out.println((i & (i - 1)) + "：普通&操作I");
        System.out.println((disturbingTermJ & (j - 1)) + "：扰动函数J");
        System.out.println((j & (j - 1)) + "：普通&操作J");
    }

    private static Object getObjectByName(String name) {
        if(name.startsWith("L")){
            return "自定义方法测试返回";
        }
        return name;
    }
}
