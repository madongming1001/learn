package com.madm.data_structure;

/**
 * 数组实现单向链表
 *
 * 参考文章：https://blog.csdn.net/h_xiao_x/article/details/109151299
 */
public class ArrayLinkedList {

    private int head = -1;
    private int tail = -1;
    private int index = -1;

    private int[] value;
    private int[] next;

    public ArrayLinkedList() {
        // 毕竟是用数组来模拟链表操作，因此我们得先假设节点数量有一定的限制
        // 我们这里只是为了了解数组实现链表的工作原理，就不考虑扩容了
        value = new int[100];
        next = new int[100];
    }

    /**
     * 头插法
     */
    public void addFirst(int val) {
        if (head < 0) {
            index = 0;
            head = 0;
            tail = 0;

            value[head] = val;
            next[head] = -1;
            return;
        }
        index++;
        value[index] = val;
        next[index] = head;
        head = index;
    }

    /**
     * 尾插法
     */
    public void addLast(int val) {
        if (tail < 0) {
            index = 0;
            tail = 0;
            head = 0;

            value[tail] = val;
            next[tail] = -1;
            return;
        }

        index++;
        value[index] = val;
        next[index] = -1;
        next[tail] = index;
        tail = index;
    }

    public void add(int val, int afterVal) {
        int res = find(afterVal);

        if (res >= 0) {
            index++;
            value[index] = val;

            int temp = next[res];
            next[res] = index;
            next[index] = temp;
        }
    }

    /**
     * 指定值位置插入
     */
    private int find(int val) {
        int idx = head;
        int res = -1;
        while (idx >= 0) {
            if (val == value[idx]) {
                res = idx;
                break;
            }
            idx = next[idx];
        }

        if (res >= 0) {
            return res;
        }

        return -1;
    }

    /**
     * 删除指定值的节点
     */
    public void remove(int removeVal) {
        //如果删除的是头节点
        if (removeVal == value[head]) {
            int newHead = next[head];
            next[head] = -1;
            head = newHead;
            return;
        }

        // 删除节点位置的前置节点
        int pre = head;
        // 要删除的节点位置
        int res = -1;

        while (pre >= 0) {


            // 判断当前节点的下一个节点的值是否是目标删除值
            if (removeVal == value[next[pre]]) {
                res = next[pre];
                break;
            }
            pre = next[pre];
        }

        if (res >= 0) {
            next[pre] = next[res];
        }
    }

    /**
     * 打印链表
     */
    public void print() {
        if (index < 0) {
            return;
        }

        int curr = head;

        do {

            int currValue = value[curr];
            System.out.print(currValue + ", ");

            curr = next[curr];

        } while (curr >= 0);
    }



}

