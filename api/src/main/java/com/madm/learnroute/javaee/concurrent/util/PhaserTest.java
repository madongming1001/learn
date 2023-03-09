package com.madm.learnroute.javaee.concurrent.util;

/**
 * Phaser 移相器属于同步器的一种，又叫阶段同步器，因为每完成一个阶段屏障才会解除，事实上他就是CyclicBarrier的升级款，在参与者数量上做了升级改良，
 * CyclicBarrier 一旦指定了参与者数量就不能更改了，但Phaser可以在使用过程中随时新增或减少插入者数量，以适应不同环境下不同的需求，拥有极高的灵活性。
 * 使用phaser可以随时新增或减少参与者数量
 * phaser主要用于可随意增加或减少参与者的高并发场景
 *
 * @author dongming.ma
 * @date 2023/3/9 10:46
 */
public class PhaserTest {
    public static void main(String[] args) {
        ReptilePhaser phaser = new ReptilePhaser(5);
        for (int i = 1; i <= 5; i++) {
            //等待并减少
            ReptileTask task = new ReptileTask(phaser);
            Thread thread = new Thread(task, i + " 号线程");
            thread.start();
        }
        phaser.register();//注册主线程
        phaser.arriveAndAwaitAdvance();//等待直到完成5个
        phaser.arriveAndDeregister();//减少任务数
        phaser.arriveAndDeregister();
        phaser.arriveAndDeregister();

        for (int i = 1; i <= 3; i++) {
            ReduceTask task = new ReduceTask(phaser);
            Thread thread = new Thread(task, i + " 号线程");
            thread.start();
        }
    }
}
