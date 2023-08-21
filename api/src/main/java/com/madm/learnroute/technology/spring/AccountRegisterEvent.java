package com.madm.learnroute.technology.spring;

import java.util.EventObject;

/**
 * @author dongming.ma
 * @date 2023/8/21 11:15
 */
public class AccountRegisterEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AccountRegisterEvent(Object source) {
        super(source);
    }
}
