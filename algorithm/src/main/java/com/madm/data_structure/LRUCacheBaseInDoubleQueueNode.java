package com.madm.data_structure;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheBaseInDoubleQueueNode {
    private int size; // 当前容量
    private int capacity; // 限制大小
    private Map<Integer, DoubleQueueNode> map; // 数据和链表中节点的映射
    private DoubleQueueNode head; // 头结点 避免null检查
    private DoubleQueueNode tail; // 尾结点 避免null检查

    public LRUCacheBaseInDoubleQueueNode(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        this.head = new DoubleQueueNode(0, 0);
        this.tail = new DoubleQueueNode(0, 0);
        this.head.next = tail;
    }

    public Integer get(Integer key) {

        DoubleQueueNode node = map.get(key);
        if (node == null) {
            return null;
        }

        // 数据在链表中，则移至链表头部
        moveToHead(node);

        return node.val;
    }

    public Integer put(Integer key, Integer value) {

        Integer oldValue;
        DoubleQueueNode node = map.get(key);
        if (node == null) {
            // 淘汰数据
            eliminate();
            // 数据不在链表中，插入数据至头部
            DoubleQueueNode newNode = new DoubleQueueNode(key, value);
            DoubleQueueNode temp = head.next;
            head.next = newNode;
            newNode.next = temp;
            newNode.pre = head;
            temp.pre = newNode;
            map.put(key, newNode);
            size++;
            oldValue = null;
        } else {
            // 数据在链表中，则移至链表头部
            moveToHead(node);
            oldValue = node.val;
            node.val = value;
        }
        return oldValue;
    }

    public Integer remove(Integer key) {
        DoubleQueueNode deletedNode = map.get(key);
        if (deletedNode == null) {
            return null;
        }
        deletedNode.pre.next = deletedNode.next;
        deletedNode.next.pre = deletedNode.pre;
        map.remove(key);
        return deletedNode.val;
    }

    // 将节点插入至头部节点
    private void moveToHead(DoubleQueueNode node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        DoubleQueueNode temp = head.next;
        head.next = node;
        node.next = temp;
        node.pre = head;
        temp.pre = node;
    }

    private void eliminate() {
        if (size < capacity) {
            return;
        }

        // 将链表中最后一个节点去除
        DoubleQueueNode last = tail.pre;
        map.remove(last.key);
        last.pre.next = tail;
        tail.pre = last.pre;
        size--;
        last = null;
    }
}

// 双向链表节点
class DoubleQueueNode {
    int key;
    int val;
    DoubleQueueNode pre;
    DoubleQueueNode next;

    public DoubleQueueNode(int key, int val) {
        this.key = key;
        this.val = val;
    }
}

