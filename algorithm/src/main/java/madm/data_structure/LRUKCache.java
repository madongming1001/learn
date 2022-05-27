package madm.data_structure;

/**
 * 如果有偶发性的批量操作，会使得热点数据被非热点数据挤出容器，使得缓存受到了“污染”。
 */
public class LRUKCache extends LRUCacheBaseInDoubleQueueNode {

    private int k; // 进入缓存队列的评判标准
    private LRUCacheBaseInDoubleQueueNode historyList; // 访问数据历史记录

    public LRUKCache(int cacheSize, int historyCapacity, int k) {
        super(cacheSize);
        this.k = k;
        this.historyList = new LRUCacheBaseInDoubleQueueNode(historyCapacity);
    }

    @Override
    public Integer get(Integer key) {

        // 记录数据访问次数
        Integer historyCount = historyList.get(key);
        historyCount = historyCount == null ? 0 : historyCount;
        historyList.put(key, ++historyCount);

        return super.get(key);
    }

    @Override
    public Integer put(Integer key, Integer value) {

        if (value == null) {
            return null;
        }

        // 如果已经在缓存里则直接返回缓存中的数据
        if (super.get(key) != null) {
            return super.put(key, value);
        }

        // 如果数据历史访问次数达到上限，则加入缓存
        Integer historyCount = historyList.get(key);
        historyCount = historyCount == null ? 0 : historyCount;
        if (historyCount >= k) {
            // 移除历史访问记录
            historyList.remove(key);
            return super.put(key, value);
        }
        return null;
    }
}

