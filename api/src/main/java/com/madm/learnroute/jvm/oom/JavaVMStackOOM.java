package com.madm.learnroute.jvm.oom;

/**
 * VM Args: -Xss2m
 */
public class JavaVMStackOOM {

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }

    private void dontStop() {
        while (true) {

        }
    }

    private void stackLeakByThread() {
        while (true) {
            new Thread(() -> {
                dontStop();
            }).start();
        }
    }

}
