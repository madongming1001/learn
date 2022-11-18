package com.madm.learnroute.javaee;

/**
 * @author dongming.ma
 * @date 2022/11/17 13:15
 */
public class ForTest {
    public static void main(String[] args) {
//        int count = 1;
//        retry:
//        for (; ; ) {
//            if (count == 5) {
//                continue retry;
//            }
//            if(count == 20){
//                break;
//            }
//            System.out.println("ssssss");
//        }
        System.out.println(testException());
    }


    public static int testException() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw e;
        }
        return 1;
    }
}
