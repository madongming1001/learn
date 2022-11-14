package com.madm.learnroute.controller;

import com.mdm.model.RestResponse;
import com.mdm.pojo.Order;
import io.protostuff.Request;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author dongming.ma
 * @date 2022/11/14 21:25
 */
@RestController
public class TransactionalController {

    @Autowired
    Source source;

    @RequestMapping("transactional")
    public RestResponse transactional() {
        Order order = new Order(1l, "transactional");
        String transactionId = UUID.randomUUID().toString();
//        Message<Order> message = MessageBuilder.withPayload(order).setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId).build();
//        source.output().send(message);
        return RestResponse.OK();
    }

}
