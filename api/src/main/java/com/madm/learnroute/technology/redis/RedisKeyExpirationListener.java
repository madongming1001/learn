package com.madm.learnroute.technology.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        // 利用redis setIfAbsent命令,如果为空set返回true,如果不为空返回false,类似setnx加锁操作
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent("SET_NX" + expiredKey, String.valueOf(System.currentTimeMillis()), 10, TimeUnit.SECONDS);
        if (aBoolean) {
            log.info("expiredKey:{}", expiredKey);
            stringRedisTemplate.delete("SET_NX" + expiredKey);
        }
    }
}
