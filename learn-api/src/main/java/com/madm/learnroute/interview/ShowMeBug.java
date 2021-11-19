package com.madm.learnroute.interview;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class ShowMeBug {
//    interface IA{
//        String getHelloName();
//    }

    public static void main(String[] args) throws Exception {
        IA ia = (IA) createObject(IA.class.getName() + "$getHelloName=Abc");
        System.out.println(ia.getHelloName());
        ia = (IA) createObject(IA.class.getName() + "$getTest=BCd");
        System.out.println(ia.getHelloName());
    }

    public static Object createObject(String str) throws Exception {
        String[] className = str.split("\\$");
        Class<?> aClass = Class.forName(className[0]);
        Method[] methods = aClass.getMethods();
        String[] methodName = className[1].split("=");
        return (IA) () -> Arrays.stream(methods).filter(m -> Objects.equals(m.getName(), methodName[0])).count() == 0 ? null : methodName[1];
    }
}
