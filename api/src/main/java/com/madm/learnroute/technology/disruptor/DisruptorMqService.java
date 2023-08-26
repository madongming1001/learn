package com.madm.learnroute.technology.disruptor;

/**
 * @author dongming.ma
 * @date 2023/1/18 17:54
 */
public interface DisruptorMqService {
    /**
     * 消息
     *
     * @param message
     */
    void sayHelloMq(String message);
}
