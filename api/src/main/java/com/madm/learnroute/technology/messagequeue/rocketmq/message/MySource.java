package com.madm.learnroute.technology.messagequeue.rocketmq.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

/**
 * @author dongming.ma
 * @date 2022/11/1 22:46
 */
public interface MySource extends Source, Sink {
    @Output("erbadagang-output")
    MessageChannel erbadagangOutput();

    @Output("trek-output")
    MessageChannel trekOutput();
}
