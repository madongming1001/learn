package com.madm.learnroute.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * 多线程执行定时任务
 *
 * @author dongming.ma
 * @date 2022/6/29 09:11
 */
@Configuration("scheduleTask")
@Slf4j
@Data
public class ScheduleThreadPoolConfig implements SchedulingConfigurer {

    @Value("${printTime.cron:1000}")
    private String cron;

    private Long timer = 10000L;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 动态使用cron表达式设置循环间隔
        taskRegistrar.addTriggerTask(() -> log.info("Current time： {}", LocalDateTime.now()), (triggerContext) -> {
            // 使用CronTrigger触发器，可动态修改cron表达式来操作循环规则
//            CronTrigger cronTrigger = new CronTrigger(cron);
//            Date nextExecutionTime = cronTrigger.nextExecutionTime(triggerContext);
            // 使用不同的触发器，为设置循环时间的关键，区别于CronTrigger触发器，该触发器可随意设置循环间隔时间，单位为毫秒
            PeriodicTrigger periodicTrigger = new PeriodicTrigger(timer);
            Date nextExecutionTime = periodicTrigger.nextExecutionTime(triggerContext);
            return nextExecutionTime;
        });
        //设定一个长度10的定时任务线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}