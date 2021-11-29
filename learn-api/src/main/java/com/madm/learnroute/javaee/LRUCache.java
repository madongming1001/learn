package com.madm.learnroute.javaee;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yideng
 */
public class LRUCache<K, V> {

    /**
     * 双链表的元素节点
     */
    private class Entry<K, V> {
        Entry<K, V> before;
        Entry<K, V> after;
        private K key;
        private V value;
    }

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
            if (e.before == null) {
                head = e.after;
            } else {
                e.before.after = e.after;
            }

            // 删除该该节点与后一个节点的联系
            if (e.after == null) {
                last = e.before;
            } else {
                e.after.before = e.before;
            }

            // 把该节点添加尾节点，判断尾节点是否为空
            if (last == null) {
                head = e;
            } else {
                e.before = last;
                last.after = e;
            }
            e.after = null;
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
            e.before = last;
            last.after = e;
        }
        tail = e;
    }

    /**
     * 删除该节点
     */
    void afterNodeRemoval(Entry<K, V> e) {
        if (e.before == null) {
            head = e.after;
        } else {
            e.before.after = e.after;
        }

        if (e.after == null) {
            tail = e.before;
        } else {
            e.after.before = e.before;
        }
    }
}
