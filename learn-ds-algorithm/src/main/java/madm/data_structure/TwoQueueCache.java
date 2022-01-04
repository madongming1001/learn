package madm.data_structure;

import java.util.LinkedHashMap;
import java.util.Map;

// 直接继承LinkedHashMap
public class TwoQueueCache extends LinkedHashMap<Integer, Integer> {

    private int k; // 进入缓存队列的评判标准
    private int historyCapacity; // 访问数据历史记录最大大小
    private LRUCacheBaseInDoubleQueueNode lruCache; // 我们前面写好的LRUCache

    public TwoQueueCache(int cacheSize, int historyCapacity, int k) {
        // 注意这里设置LinkedHashMap的accessOrder为false
        super();
        this.historyCapacity = historyCapacity;
        this.k = k;
        this.lruCache = new LRUCacheBaseInDoubleQueueNode(cacheSize);
    }

    public Integer get(Integer key) {
        // 记录数据访问记录
        Integer historyCount = super.get(key);
        historyCount = historyCount == null ? 0 : historyCount;
        super.put(key, ++historyCount);
        return lruCache.get(key);
    }

    public Integer put(Integer key, Integer value) {

        if (value == null) {
            return null;
        }

        // 如果已经在缓存里则直接返回缓存中的数据
        if (lruCache.get(key) != null) {
            return lruCache.put(key, value);
        }

        // 如果数据历史访问次数达到上限，则加入缓存
        Integer historyCount = super.get(key);
        historyCount = historyCount == null ? 0 : historyCount;
        if (historyCount >= k) {
            // 移除历史访问记录
            super.remove(key);
            return lruCache.put(key, value);
        }

        return null;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return super.size() >= historyCapacity;
    }
}

