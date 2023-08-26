package com.madm.learnroute.javaee;

public class NoVisibility {
    private static boolean ready = false;
    private static int number;

    public static void main(String[] args) {
        ReaderThread thread = new ReaderThread();
        thread.start();
        number = 42;
        ready = true;
    }

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
                System.out.println(number);
            }
        }
    }
}