package com.madm.learnroute.jvm.os;

import lombok.extern.slf4j.Slf4j;

/**
 * 指令重排
 *
 * ① 编译器优化的重排序。编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序。
 * ② 指令级并行的重排序。现代处理器采用了指令级并行技术（Instruction-Level Parallelism，ILP）来将多条指令重叠执行。如果不存在数据依赖性，处理器可以改变语句对应 机器指令的执行顺序。
 * ③ 内存系统的重排序。由于处理器使用缓存和读/写缓冲区，这使得加载和存储操作看上 去可能是在乱序执行。
 */
@Slf4j
public class InstructionRearrangement {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    shortWait(10000);
                    a = 1;
                    x = b;
                }
            });
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    y = a;
                }
            });
            t1.start();
            t1.run();
            t2.start();
            t1.join();
            t2.join();

            String result = "第" + i + "次 (" + x + "," + y + ")";
            if (x == 0 && y == 0) {
                System.out.println(result);
                break;
            } else {
            }
        }
    }

    private static void shortWait(int i) {

    }
}
