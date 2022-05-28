package com.madm.learnroute.javaee;

public class JavaNativeInterfacePractice {

    public JavaNativeInterfacePractice() {
        System.out.println("构造方法");
    }

    //所有native关键词修饰的都是对本地的声明
    public void displayHelloWorld() {
        System.out.println("displayHelloWorld");
    }

    static {
        System.out.println("静态代码块输出");
    }

    {
        System.out.println("空代码块输出");
    }

    public static String getSelfIP() {
        return "selfIP";
    }

    public static void main(String[] args) {
        System.out.println(JavaNativeInterfacePractice.getSelfIP());
    }

}
