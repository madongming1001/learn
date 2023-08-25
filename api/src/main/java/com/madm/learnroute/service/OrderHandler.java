package com.madm.learnroute.service;

import org.springframework.stereotype.Component;

/**
 * @author dongming.ma
 * @date 2023/8/25 00:01
 */
@Component
public class OrderHandler extends AbstractHandler {
    @Override
    String getType() {
        return this.getClass().getSimpleName();
    }
}
