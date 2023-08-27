package com.madm.learnroute.controller;

import com.mdm.model.RestResponse;
import com.mdm.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * @author dongming.ma
 * @date 2022/11/14 21:25
 */
//@RestController
public class TransactionalController {

    @Autowired
    Source source;

    @RequestMapping("transactional")
    public RestResponse transactional() {
        Order order = new Order(1L, "transactional");
        String transactionId = UUID.randomUUID().toString();
//        Message<Order> message = MessageBuilder.withPayload(order).setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId).build();
//        source.output().send(message);
        return RestResponse.OK();
    }

}
