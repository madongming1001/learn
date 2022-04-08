package com.madm.learnroute.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;

public class RedissonBloomFilterTest {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://47.95.220.43:6379").setPassword("badguy").setTimeout(5000);
        Redisson redisson = (Redisson) Redisson.create(config);
        System.out.println(redisson.getId());
//        RAtomicLong atomicLong = redisson.getAtomicLong("111");
//        System.out.println(atomicLong.get());
//        RLock lock = redisson.getLock("lock");
//        lock.lock();
//        lock.unlock();
//        redisson.getScript().eval(RScript.Mode.READ_ONLY,"", RScript.ReturnType.BOOLEAN);
//
//
//        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("nameList");
//        //初始化布隆过滤器：预计元素为100000000L,误差率为3%,根据这两个参数会计算出底层的bit数组大小 18
//        bloomFilter.tryInit(100000000L, 0.03);
//        //将zhuge插入到布隆过滤器中
//        bloomFilter.add("zhuge");
//        //判断下面号码是否在布隆过滤器中
//        System.out.println(bloomFilter.contains("guojia"));//false
//        System.out.println(bloomFilter.contains("baiqi"));//false
//        System.out.println(bloomFilter.contains("zhuge"));//true
    }
}
