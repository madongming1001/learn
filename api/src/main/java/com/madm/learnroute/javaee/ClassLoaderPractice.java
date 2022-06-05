package com.madm.learnroute.javaee;

public class ClassLoaderPractice {
    public static void main(String[] args) {
        // 出发都是舍弃小数位
//        System.out.println(String.class.getClassLoader());
//        System.out.println(ClassLoaderPractice.class.getClassLoader());
//        System.out.println(7 / 2);
//        System.out.println(7 >>> 1);
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/com/madm/learnroute/javaee/ 当前同级目录下去找
        System.out.println(ClassLoaderPractice.class.getResource("logback-spring.xml"));
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/ 上层目录下去找
        System.out.println(ClassLoaderPractice.class.getResource("/logback-spring.xml"));
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/ 上层目录下去找
        System.out.println(ClassLoaderPractice.class.getClassLoader().getResource("logback-spring.xml"));
        //null 同级目录下去找
        System.out.println(ClassLoaderPractice.class.getClassLoader().getResource("/logback-spring.xml"));
    }
}
