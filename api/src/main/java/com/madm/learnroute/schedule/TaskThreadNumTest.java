package com.madm.learnroute.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2023/9/13 09:18
 */

@Slf4j
@Component
//实现 SchedulingConfigurer 接口多线程执行各个定时任务互不影响 但是同一个定时任务还是会存在上一个任务执行时间超长，下一个会被阻塞的问题
public class TaskThreadNumTest {

    @Scheduled(fixedDelay = 1000 * 15)
    public void task1() throws InterruptedException {
        log.info("Task1 start");
        TimeUnit.SECONDS.sleep(9);
        log.info("Task1 end");
    }

    @Scheduled(fixedDelay = 1000 * 2)
    public void task2() {
        log.info("Task2 ok");
    }

}
