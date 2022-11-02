package com.mdm.consumer.message;

import com.mdm.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author dongming.ma
 * @date 2022/11/1 23:07
 */
@Component
@Slf4j
public class MQConsumer {

    @StreamListener(MySink.ERBADAGANG_INPUT)
    public void onMessage(@Payload User message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @StreamListener(MySink.TREK_INPUT)
    public void onTrekMessage(@Payload User message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
