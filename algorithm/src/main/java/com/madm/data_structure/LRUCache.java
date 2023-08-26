package com.madm.data_structure;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LRUCache<K, V> {
    Node<K, V> head;
    Node<K, V> tail;
    private Integer capacity;
    private Map<K, Node<K, V>> caches;
    public LRUCache(int capacity) {
        if (capacity < 1) {
            throw new RuntimeException("should be more than 0.");
        }
        caches = new HashMap<>();
        this.capacity = capacity;
    }

    public void set(K key, V value) {
        Node node = new Node(key, value);

    }

    public Node get(K key) {
        Node<K, V> node = this.caches.get(key);
        return node;
    }

    public void moveNodeToTail(Node node) {
        if (tail == node) {
            return;
        }
        if (head == node) {
            this.head = node.next;
            this.head.pre = null;
        } else {
            //把节点从链中拿掉
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        node.pre = tail;
        node.next = null;
        tail.next = node;
        tail = node;
    }

    public void removeHeadNode() {
        if (head == null) {
            return;
        }
        Node res = head;
        if (head == tail) {
            this.head = null;
            this.tail = null;
        } else {
            this.head = res.next;
            res.next = null;//Help GC
            this.head.pre = null;
        }
    }

    public void addNode(Node<K, V> newNode) {
        if (newNode == null) {
            return;
        }
        if (head == null) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            tail.next = newNode;
            newNode.pre = tail;
            tail = newNode;
        }
    }

    class Node<K, V> {
        K key;
        V value;
        Node<K, V> pre;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
