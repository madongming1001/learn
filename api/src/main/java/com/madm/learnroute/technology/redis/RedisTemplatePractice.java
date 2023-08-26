package com.madm.learnroute.technology.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class RedisTemplatePractice {

    @Autowired
    RedisTemplate redisTemplate;

    @PostConstruct
    public void setRedisTemplate() {
        redisTemplate.opsForValue().set("444444", 4L);

        List<Map<String, Object>> candidate = new ArrayList<>();
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("1", "1");
        Map<String, Object> stringObjectHashMap1 = new HashMap<>();
        stringObjectHashMap1.put("1", "2");
        Map<String, Object> stringObjectHashMap2 = new HashMap<>();
        stringObjectHashMap2.put("1", "3");
        Map<String, Object> stringObjectHashMap3 = new HashMap<>();
        stringObjectHashMap3.put("1", "4");
        candidate.add(stringObjectHashMap);
        candidate.add(stringObjectHashMap1);
        candidate.add(stringObjectHashMap2);
        candidate.add(stringObjectHashMap3);

        redisTemplate.delete("1");

        Long lastMachMasterId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get("444444")));
        lastMachMasterId = Objects.isNull(lastMachMasterId) ? 0L : lastMachMasterId;
        Long finalLastMachMasterId = lastMachMasterId;
        final Long[] aLong = {null};
        candidate.stream().filter(m -> {
            return Long.valueOf(String.valueOf(m.get("1"))) > finalLastMachMasterId;
        }).limit(1).forEach(n -> {
            aLong[0] = Long.valueOf(String.valueOf(n.get("1")));
        });
    }
}
