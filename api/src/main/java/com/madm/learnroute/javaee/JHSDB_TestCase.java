//package com.madm.learnroute.javaee;
//
//import org.omg.CORBA.ObjectHolder;
//
///**
// * staticObj,instanceObj,localObj存放在哪里？
// */
//public class JHSDB_TestCase {
//    static class TestMain{
//        static ObjectHolder staticObj = new ObjectHolder();
//        ObjectHolder instanceObj = new ObjectHolder();
//        void foo(){
//            ObjectHolder localObj = new ObjectHolder();
//            System.out.println("done");
//        }
//    }
//
//    public static void main(String[] args) {
//        TestMain test = new TestMain();
//        test.foo();
//    }
//}
