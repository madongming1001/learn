package com.madm.learnroute.technology.spring;

import org.springframework.context.ApplicationEvent;

public class BusinessEvent extends ApplicationEvent {

    private String type;

    public BusinessEvent(MyObject source, String type) {
        super(source);
    }
}