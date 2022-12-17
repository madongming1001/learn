package com.madm.learnroute.technology.redis;

import com.google.common.base.Strings;
import com.mdm.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * @author dongming.ma
 * @date 2022/10/29 17:04
 */
public class BloomFilterPractice {
    public static void main(String[] args) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(StringUtils.EMPTY);
        RedissonClient redisson = Redisson.create(config);
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("commodity");

        RLock rLock = readWriteLock.readLock();
        RLock wLock = readWriteLock.writeLock();






        RBloomFilter<User> bloomFilter = redisson.getBloomFilter("user");
        // 初始化布隆过滤器，预计统计元素数量为55000000，期望误差率为0.03
        bloomFilter.tryInit(55000000L, 0.03);
        bloomFilter.add(new User(1));
        bloomFilter.add(new User(2));
        if (bloomFilter.contains(new User(2))) {
            System.out.println("bloom包含对应的key");
        }
    }
}
