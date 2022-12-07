package com.madm.learnroute.technology.redis;

import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2022/12/6 18:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class RBlockingQueuePracticeTest {

    @Test
    public void test01() throws IOException {
        HashedWheelTimer timer = new HashedWheelTimer();    //使用默认参数
        log.info("start");
        timer.newTimeout(timeout -> log.info("running"), 3, TimeUnit.SECONDS);   //延时3秒后开始执行

        System.in.read();
    }

    @Test
    public void test02() throws IOException {
        HashedWheelTimer timer = new HashedWheelTimer();
        log.info("start");
        timer.newTimeout(timeout -> {
            log.info("running");
            Thread.sleep(2000);
            log.info("end");
        }, 1, TimeUnit.SECONDS);

        timer.newTimeout(timeout -> {
            log.info("running");
            Thread.sleep(2000);
            log.info("end");
        }, 1, TimeUnit.SECONDS);

        System.in.read();
    }


}