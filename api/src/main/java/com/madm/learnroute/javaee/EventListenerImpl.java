package com.madm.learnroute.javaee;


import com.mdm.listener.EventListener;

/**
 * @author dongming.ma
 * @date 2022/6/11 15:13
 */
public class EventListenerImpl implements EventListener {

    @Override
    public void onEvent() {
        System.out.println("EventListenerImpl");
    }
}
