package com.madm.learnroute.javaee;

import com.madm.learnroute.pojo.Apple;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ABAProblemExample {
    static class Stack {

        private AtomicReference<Node> top = new AtomicReference<>();

        static class Node {
            String value;

            Node next;

            public Node(String value) {
                this.value = value;
            }
        }

        //出栈
        public Node pop(int time) {
            Node newTop;
            Node oldTop;

            do {
                oldTop = top.get();
                if (oldTop == null) {
                    return null;
                }
                newTop = oldTop.next;
                try {
                    //休眠一段时间，模拟ABA问题
                    TimeUnit.SECONDS.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!top.compareAndSet(oldTop, newTop));
            return oldTop;
        }

        public void push(Node node) {
            Node oldTop;
            do {
                oldTop = top.get();
                node.next = oldTop;
            } while (!top.compareAndSet(oldTop, node));
        }

        public AtomicReference<Node> getTop() {
            return top;
        }


    }
    public static void main(String[] args) throws InterruptedException {
        Stack stack = new Stack();
        Stack.Node a = new Stack.Node("A");
        Stack.Node b = new Stack.Node("B");
        // 初始化栈结构
        stack.push(b);
        stack.push(a);

        // ABA 测试
        Thread t1 = new Thread(() -> {
            stack.pop(2);
        });

        Stack.Node c = new Stack.Node("C");
        Stack.Node d = new Stack.Node("D");
        Thread t2 = new Thread(() -> {
            //出栈操作
            stack.pop(0);
            stack.pop(0);
            //入栈操作
            stack.push(d);
            stack.push(c);
            stack.push(a);
        });
        //
        t1.start();
        t2.start();

        //沉睡主线程
        TimeUnit.SECONDS.sleep(5);

        Stack.Node top = stack.getTop().get();
        do {
            System.out.println(top.value);
            top = top.next;
        } while (top != null);
    }
}