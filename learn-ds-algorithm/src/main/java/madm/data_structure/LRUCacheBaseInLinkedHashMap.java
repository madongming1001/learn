package madm.data_structure;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheBaseInLinkedHashMap extends LinkedHashMap {

    private int capacity;

    public LRUCacheBaseInLinkedHashMap(int capacity) {
        // 注意这里将LinkedHashMap的accessOrder设为true
        super(16, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return super.size() >= capacity;
    }
}


