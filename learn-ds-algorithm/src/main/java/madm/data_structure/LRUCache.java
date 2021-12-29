package madm.data_structure;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
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

    Node<K, V> head;
    Node<K, V> tail;

    private Integer capacity;
    private Map<K, Node<K, V>> caches = new HashMap<>();

    public LRUCache(int capacity) {
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
    }
}
