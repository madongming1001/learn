package com.madm.learnroute.technology.spring;

public class MyObject {
    public static void getObject(ObjectFactory<?> objectFactory) {
        System.out.println(objectFactory.getObject());
    }

    private static Object getObjectByName(String name) {
        if (name.startsWith("L")) {
            return "自定义方法测试返回";
        }
        return name;
    }
}
