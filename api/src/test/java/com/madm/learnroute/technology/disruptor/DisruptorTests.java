package com.madm.learnroute.technology.disruptor;

import com.madm.learnroute.LearnApiApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dongming.ma
 * @date 2023/1/18 17:56
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnApiApplication.class)
public class DisruptorTests {

    @Autowired
    private DisruptorMqService disruptorMqService;

    /**
     * 项目内部使用Disruptor做消息队列
     *
     * @throws Exception
     */
    @Test
    public void sayHelloMqTest() throws Exception {
        disruptorMqService.sayHelloMq("消息到了，Hello world!");
        log.info("消息队列已发送完毕");
        //这里停止2000ms是为了确定是处理消息是异步的
        Thread.sleep(2000);
    }
}