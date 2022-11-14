package com.mdm.consumer.controller;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @author dongming.ma
 * @date 2022/11/14 21:40
 */
public class ConsumerController {

    @StreamListener(value = Sink.INPUT)
    public void receive(String receiveMsg) {

    }

}
