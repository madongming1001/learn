package com.madm.learnroute.javaee;

/**
 * @author dongming.ma
 * @date 2022/7/5 16:07
 */
public class BitwiseOperatorsPractice {
    public static void main(String[] args) {
//        System.out.println(Integer.valueOf("00111100",2));
//        System.out.println(Integer.valueOf("00001101",2));
        int A = 60;//0011 1100
        int B = 13;//0000 1101
        System.out.println((A ^ B) | B);
        System.out.println("A & B : " + (A & B));//0000 1100;
        System.out.println("A | B : " + (A | B));//0011 1101
        System.out.println("A ^ B : " + (A ^ B));//0011 0001
        System.out.println("~A : " + ~A);//1100 0011
    }
}
