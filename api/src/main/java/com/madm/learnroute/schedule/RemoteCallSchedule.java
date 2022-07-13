package com.madm.learnroute.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author dongming.ma
 * @date 2022/7/13 11:26
 */
@Slf4j
public class RemoteCallSchedule {

    @Value("${allowCall:false}")
    private boolean allowCall;


    @Scheduled(cron = "*/5 * * * * ?")
    public void timedCall() {
        if (allowCall) {
            log.debug("调用远程接口");
        }
    }
}
