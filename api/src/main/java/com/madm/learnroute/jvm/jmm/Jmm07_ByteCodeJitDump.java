package com.madm.learnroute.jvm.jmm;

/**
 * ,;,,;
 * ,;;'(    社
 * __      ,;;' ' \   会
 * /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 * ,;' \    /-.,,(   ) \    码
 * ) /       ) / )|    农
 * ||        ||  \)
 * (_\       (_\
 *
 * @author ：杨过
 * @date ：Created in 2020/4/29 14:12
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description: -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*Jmm07_ByteCodeJitDump.refresh
 **/
public class Jmm07_ByteCodeJitDump {
    private static volatile int c = 1;

    public static int refresh() {
        int a = 0;
        int b = 1;
        int sub = a + b + c;


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });


        return sub;
    }

    public static void main(String[] args) throws InterruptedException {

        Thread thread0 = new Thread(() -> {
            System.out.println(String.format("sub0:%d", refresh()));
        });

        thread0.start();

        Thread thread1 = new Thread(() -> {
            System.out.println(String.format("sub1:%d", refresh()));
        });

        thread1.start();
    }
}
