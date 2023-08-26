package com.madm.learnroute.service;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2023/8/26 15:28
 */
@Service
public class LimitRateService {

    @Autowired
    RedisTemplate redisTemplate;

    private void fixedTimeWindow() {
        //根据前端传递的qps上线
        Integer times = 2;
        String redisKey = "redisQps";
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(redisKey, redisTemplate.getConnectionFactory());
        int no = redisAtomicInteger.getAndIncrement();
        //设置时间固定时间窗口长度 1S
        if (no == 0) {
            redisAtomicInteger.expire(1, TimeUnit.SECONDS);
        }
        //判断是否超限  time=2 表示qps=3
        if (no > times) {
            throw new RuntimeException("qps refuse request");
        }
    }

    private void slidingTimeWindow() {
        String redisKey = "qpsZset";
        Integer times = 100;
        long currentTimeMillis = System.currentTimeMillis();
        long interMills = 1000L;
        Long count = redisTemplate.opsForZSet().count(redisKey, currentTimeMillis - interMills, currentTimeMillis);
        if (count > times) {
            throw new RuntimeException("qps refuse request");
        }
        redisTemplate.opsForZSet().add(redisKey, IdUtil.fastSimpleUUID(), currentTimeMillis);
    }

}
