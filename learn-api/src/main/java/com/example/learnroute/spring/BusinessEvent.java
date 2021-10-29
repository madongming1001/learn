package com.example.learnroute.spring;

import org.springframework.context.ApplicationEvent;

public class BusinessEvent extends ApplicationEvent {

    private String type;

    public BusinessEvent(Object source,String type) {
        super(source);
    }
}