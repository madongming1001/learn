package com.madm.design.strategy.classmethod;

public class Content {
    Strategy strategy;

    public Content(Strategy strategy) {
        this.strategy = strategy;
    }

    public String contentStrategy(String resourceId) {
        return strategy.query(resourceId);
    }

}
