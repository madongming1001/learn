package com.madm.learnroute.javaee.concurrent.synchronize;

/**
 * 查看JIT编译结果
 * -XX:+UnlockDiagnosticVMOptions
 * -XX:+PrintAssembly -Xcomp
 * -XX:CompileCommand=dontinline,*Bar.sum
 * -XX:CompileCommand=compileonly,*Bar.sum
 * <p>
 * /Users/madongming/software/hsdis
 */
public class T0_ObjectStackAlloc {

    /**
     * 进行两种测试
     * 关闭逃逸分析，同时调大堆空间，避免堆内GC的发生，如果有GC信息将会被打印出来
     * VM运行参数：-Xmx4G -Xms4G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 开启逃逸分析
     * VM运行参数：-Xmx4G -Xms4G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 执行main方法后
     * jps 查看进程
     * jmap -histo 进程ID
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            alloc();
        }
        long end = System.currentTimeMillis();
        //查看执行时间
        System.out.println("cost-time " + (end - start) + " ms");
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

    private static TulingStudent alloc() {
        //Jit对编译时会对代码进行 逃逸分析
        //并不是所有对象存放在堆区，有的一部分存在线程栈空间
        TulingStudent student = new TulingStudent();
        return student;
    }

    static class TulingStudent {
        private long id;
        private int age;
    }
}
