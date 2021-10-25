package com.example.learnroute.javaee;

import java.util.Scanner;

public class IsTwoPowerPractice {

    public static void main(String[] args) {
        boolean isTwoPower = true;
        while (isTwoPower) {
            Scanner scanner = new Scanner(System.in);
            int i = scanner.nextInt();
            boolean twoPower = isTwoPower(i);
            System.out.println(twoPower);
            if (twoPower) {
                isTwoPower = false;
            }
        }
    }

    private static boolean isTwoPower(int i) {
        if ((i - 1 & i) == 0) {
            return true;
        }
        return false;
    }
}
