package com.madm.learnroute.javaee;

class ThreadLocalTask extends Thread {
    ThreadLocal<Integer> threadLocal;
    Integer threadId;

    {
        threadLocal = new ThreadLocal<>();
    }
    public ThreadLocalTask(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        threadLocal.set(threadId);
        System.out.println("threadLocal值为：" + threadLocal.get() + "，线程id为：" + threadId);
    }
}

public class ThreadLocalPractice {
    public static void main(String[] args) {
        new Thread(new ThreadLocalTask(1)).start();
        new Thread(new ThreadLocalTask(2)).start();
    }
}
