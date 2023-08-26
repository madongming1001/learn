package com.madm.design.strategy.enumlambda.celue;


public enum StrategySelector {

    strategyA(1, "strategyA"),
    strategyB(2, "strategyB"),
    strategyC(3, "strategyC");

    private Integer code;
    private String strategy;

    StrategySelector(Integer code, String strategy) {
        this.code = code;
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }

    public Integer getCode() {
        return code;
    }
}

