package com.madm.learnroute.javaee;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yideng
 */
public class LRUCache<K, V> {

    /**
     * 缓存容量大小
     */
    private Integer capacity;
    /**
     * 头结点
     */
    private Entry<K, V> head;
    /**
     * 尾节点
     */
    private Entry<K, V> tail;
    /**
     * 用来存储所有元素
     */
    private Map<K, Entry<K, V>> caches = new HashMap<>();
    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public static void main(String[] args) {
        char c = 110;
        System.out.println((char) 110);
        System.out.println(c ^ 32);
        System.out.println(Integer.toBinaryString(c));
        System.out.println(Integer.toBinaryString(32));
        System.out.println(Integer.valueOf("100_1110", 2));
        System.out.println("=====================================");
        System.out.println(Integer.valueOf("1001110", 2));
        System.out.println(Integer.toBinaryString(1101110));
        System.out.println(Integer.valueOf("1001110", 2));
        System.out.println(Integer.valueOf("0101110", 2));
    }

    public V get(K key) {
        final Entry<K, V> node = caches.get(key);
        if (node != null) {
            // 有访问，就移到链表末尾
            afterNodeAccess(node);
            return node.value;
        }
        return null;
    }

    /**
     * 把该元素移到末尾
     */
    private void afterNodeAccess(Entry<K, V> e) {
        Entry<K, V> last = tail;
        // 如果e不是尾节点，才需要移动
        if (last != e) {
            // 删除该该节点与前一个节点的联系，判断是不是头结点
            if (e.pre == null) {
                head = e.next;
            } else {
                e.pre.next = e.next;
            }

            // 删除该该节点与后一个节点的联系
            if (e.next == null) {
                last = e.pre;
            } else {
                e.next.pre = e.pre;
            }

            // 把该节点添加尾节点，判断尾节点是否为空
            if (last == null) {
                head = e;
            } else {
                e.pre = last;
                last.next = e;
            }
            e.next = null;
            tail = e;
        }
    }

    public V put(K key, V value) {
        Entry<K, V> entry = caches.get(key);
        if (entry == null) {
            entry = new Entry<>();
            entry.key = key;
            entry.value = value;
            // 新节点添加到末尾
            linkNodeLast(entry);
            caches.put(key, entry);
            // 节点数大于容量，就删除头节点
            if (this.caches.size() > this.capacity) {
                this.caches.remove(head.key);
                afterNodeRemoval(head);
            }
            return null;
        }
        entry.value = value;
        // 节点有更新就移动到未节点
        afterNodeAccess(entry);
        caches.put(key, entry);
        return entry.value;
    }

    /**
     * 把该节点添加到尾节点
     */
    private void linkNodeLast(Entry<K, V> e) {
        final Entry<K, V> last = this.tail;
        if (head == null) {
            head = e;
        } else {
            e.pre = last;
            last.next = e;
        }
        tail = e;
    }

    /**
     * 删除该节点
     */
    void afterNodeRemoval(Entry<K, V> e) {
        if (e.pre == null) {
            head = e.next;
        } else {
            e.pre.next = e.next;
        }

        if (e.next == null) {
            tail = e.pre;
        } else {
            e.next.pre = e.pre;
        }
    }

    /**
     * 双链表的元素节点
     */
    private class Entry<K, V> {
        Entry<K, V> pre;
        Entry<K, V> next;
        private K key;
        private V value;
    }
}
