package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.config.ScheduleThreadPoolConfig;
import com.mdm.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
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
public class ScheduleController /*extends TransactionAspectSupport */ {

    @Autowired
    ScheduleThreadPoolConfig scheduleTask;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/updateCron")
    @Transactional
    public RestResponse updateCron(String... cron) {
        log.info("new cron :{}", cron);
//        jdbcTemplate.update("update account set username = left(username,9) where id = " + 1);
//        System.out.println(currentTransactionInfo().getTransactionStatus());
        redisTemplate.opsForValue().setIfAbsent("count", 1);
        // null when used in pipeline / transaction.
        redisTemplate.opsForValue().increment("count");
        if (ArrayUtil.isEmpty(cron)) {
            scheduleTask.setCron(cron[0]);
        }
        return RestResponse.OK();
    }

    @GetMapping("/updateTimer")
    public RestResponse updateTimer(Long timer) {
        log.info("new timer :{}", timer);
        scheduleTask.setTimer(timer);
        return RestResponse.OK();
    }

}
