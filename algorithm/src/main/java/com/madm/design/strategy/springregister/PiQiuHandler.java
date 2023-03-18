package com.woniu.design;

import org.springframework.stereotype.Component;

@Component
public class PiQiuHandler extends AbstractHandler {
    @Override
    public void AA(String nikeName) {
        //业务逻辑
        System.out.println("我是皮球");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Factory2.register("皮球", this);
    }
}
