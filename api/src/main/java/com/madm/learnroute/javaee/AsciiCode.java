package com.madm.learnroute.javaee;

public class AsciiCode {
    public static void main(String[] args) {
//        String str = "lies";
//        System.out.println(str.hashCode());
//        char[] chars1 = "lies".toCharArray();
//        char[] chars2 = "foes".toCharArray();
//        for (int i = 0; i < chars1.length; i++) {
//            System.out.print(chars1[i] + ":"+(int)chars1[i] + " ");
//        }
//        System.out.println("\t");
//        for (int i = 0; i < chars2.length; i++) {
//            System.out.print(chars2[i] + ":"+(int)chars2[i] + " ");
//        }
        //GBK字符集编码格式中文两字节
        //UTF-8字符集编码三字节
        System.out.println((int) (Math.random() * 88) + 1);
    }
}
