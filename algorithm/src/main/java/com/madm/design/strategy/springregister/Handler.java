package com.woniu.design;

import org.springframework.beans.factory.InitializingBean;

/**
 * 策略设计模式
 */
public interface Handler extends InitializingBean {
    void AA(String nikeName);


}
