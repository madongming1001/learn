package com.madm.learnroute.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author dongming.ma
 * @date 2023/8/24 23:57
 */
@Component
@Slf4j
public class AccountHandler extends AbstractHandler {
    @Override
    String getType() {
        return this.getClass().getSimpleName();
    }

    @PostConstruct
    private void init() {
        log.info("子类init方法测试和父类的init方法会出现什么样的状况");
    }
}
