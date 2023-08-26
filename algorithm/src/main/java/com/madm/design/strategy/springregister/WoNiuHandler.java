package com.madm.design.strategy.springregister;

import org.springframework.stereotype.Component;

@Component
public class WoNiuHandler extends AbstractHandler {

    @Override
    public void AA(String nikeName) {
        //业务逻辑
        System.out.println("我是蜗牛");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Factory2.register("蜗牛", this);
    }
}
