package com.madm.learnroute.javaee;

import com.mdm.listener.EventListener;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 3:13 PM
 */
public class ServiceLoaderPractice {
    public static void main(String[] args) {
        ServiceLoader<EventListener> load = ServiceLoader.load(EventListener.class);
        Iterator<EventListener> iterator = load.iterator();
        while (iterator.hasNext()) {
            EventListener el = iterator.next();
            el.onEvent();
        }
    }
}