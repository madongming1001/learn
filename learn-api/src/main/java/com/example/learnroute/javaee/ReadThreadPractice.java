package com.example.learnroute.javaee;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReadThreadPractice extends Thread {

    public static int runNum = 1;
    public static boolean ready = false;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (ready) {
                System.out.println("readThreadPractice线程的值为：" + runNum);
            }
        }
        System.out.println("ReadThreadPractice线程运行了，时间是："+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
    }
    static class WriteThreadPractice extends Thread{
        @Override
        public void run() {
            runNum = 0;
            ready = true;
            System.out.println("WriteThreadPractice线程运行了，时间是："+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadThreadPractice read = new ReadThreadPractice();
//        WriteThreadPractice write = new WriteThreadPractice();
//
//        write.start();
//        read.start();
//
//        Thread.sleep(10L);
//        read.interrupt();
//        System.out.println("main exit");
    }

}
