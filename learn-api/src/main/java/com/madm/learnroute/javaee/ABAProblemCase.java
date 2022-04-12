package com.madm.learnroute.javaee;

import java.util.concurrent.atomic.AtomicReference;

public class ABAProblemCase {

    static class Stack {
        // 将top放在原子类中
        private AtomicReference<Node> top = new AtomicReference<>();

        // 栈中节点信息
        static class Node {
            int value;
            Node next;

            public Node(int value) {
                this.value = value;
            }
        }

        // 出栈操作
        public Node pop() {
            for (; ; ) {
                // 获取栈顶节点
                Node t = top.get();
                if (t == null) {
                    return null;
                }
                // 栈顶下一个节点
                Node next = t.next;
                // CAS更新top指向其next节点
                if (top.compareAndSet(t, next)) {
                    // 把栈顶元素弹出，应该把next清空防止外面直接操作栈
                    t.next = null;// help gc
                    return t;
                }
            }
        }

        // 入栈操作
        public void push(Node node) {
            for (; ; ) {
                // 获取栈顶节点
                Node next = top.get();
                // 设置栈顶节点为新节点的next节点
                node.next = next;
                // CAS更新top指向新节点
                if (top.compareAndSet(next, node)) {
                    return;
                }
            }
        }
    }
}

