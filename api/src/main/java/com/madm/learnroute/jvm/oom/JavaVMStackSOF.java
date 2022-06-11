package com.madm.learnroute.jvm.oom;

/**
 * VM args: -Xss160k 根据操作系统和jdk版本的因素决定了默认初始化大小 current environment jdk 1.8 mac Catalina
 * specified 明确规定
 * stack over flower 两种情况
 * 1.由于栈帧太大
 * 2.由于虚拟机栈容量太小
 * hotspot虚拟机的选择时不支持动态扩展栈空间
 */
public class JavaVMStackSOF {
    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF sof = new JavaVMStackSOF();
        try {
            sof.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length: " + sof.stackLength);
            e.printStackTrace();
        }
    }

}
