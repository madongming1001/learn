package com.madm.learnroute.javaee;

public class AssertPractice {
    int value = 3;
    boolean isFinsh = false;

    public static void main(String[] args) {
        AssertPractice ap = new AssertPractice();
        ap.exeToCPUA();
        ap.exeToCPUB();
    }

    void exeToCPUA() {
        value = 10;
        isFinsh = true;
    }

    void exeToCPUB() {
        if (isFinsh) {
            //value一定等于10？！
            assert value == 10;
        }
    }
}
