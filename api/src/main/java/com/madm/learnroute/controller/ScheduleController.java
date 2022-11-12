package com.madm.learnroute.controller;

import com.madm.learnroute.config.ScheduleThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
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
public class ScheduleController {

    ScheduleThreadPoolConfig scheduleTask;

    @GetMapping("/updateCron")
    public String updateCron(String cron) {
        log.info("new cron :{}", cron);
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
