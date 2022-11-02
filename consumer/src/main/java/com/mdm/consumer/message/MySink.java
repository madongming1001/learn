package com.mdm.consumer.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author dongming.ma
 * @date 2022/11/1 23:03
 */
public interface MySink {
    String ERBADAGANG_INPUT = "erbadagang-input";
    String TREK_INPUT = "trek-input";

    @Input(ERBADAGANG_INPUT)
    SubscribableChannel demo01Input();

    @Input(TREK_INPUT)
    SubscribableChannel trekInput();

}
