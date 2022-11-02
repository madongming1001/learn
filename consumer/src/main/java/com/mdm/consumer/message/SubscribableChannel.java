package com.mdm.consumer.message;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author dongming.ma
 * @date 2022/11/1 23:05
 */
public interface SubscribableChannel extends MessageChannel {

    boolean subscribe(MessageHandler handler); // 订阅

    boolean unsubscribe(MessageHandler handler); // 取消订阅

}
