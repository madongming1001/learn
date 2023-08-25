package com.madm.learnroute.service;

import javax.annotation.PostConstruct;

/**
 * @author dongming.ma
 * @date 2023/8/24 23:56
 */
public abstract class AbstractHandler {

    @PostConstruct
    private void init() {
        HandlerFactory.register(getType(), this);
    }

    abstract String getType();
}
