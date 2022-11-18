package com.madm.learnroute.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.madm.learnroute.config.ScheduleThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dongming.ma
 * @date 2022/11/12 13:49
 */

@RestController
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController /*extends TransactionAspectSupport */{

    @Autowired
    ScheduleThreadPoolConfig scheduleTask;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/updateCron")
    @Transactional
    public String updateCron(String cron) {
        log.info("new cron :{}", cron);
//        jdbcTemplate.update("update account set username = left(username,9) where id = " + 1);
//        System.out.println(currentTransactionInfo().getTransactionStatus());
        redisTemplate.opsForValue().setIfAbsent("count", 1);
        // null when used in pipeline / transaction.
        redisTemplate.opsForValue().increment("count");
        scheduleTask.setCron(cron);
        return "ok";
    }

    @GetMapping("/updateTimer")
    public String updateTimer(Long timer) {
        log.info("new timer :{}", timer);
        scheduleTask.setTimer(timer);
        return "ok";
    }

}
