package com.madm.learnroute.technology.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author dongming.ma
 * @date 2023/1/18 17:54
 */
public class HelloEventFactory implements EventFactory<MessageModel> {
    @Override
    public MessageModel newInstance() {
        return new MessageModel();
    }
}
