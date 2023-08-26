package com.madm.design.strategy.enumlambda.celue;

public class ConcreteStrategyA implements Strategy {

    @Override
    public String strategy() {
        return StrategySelector.strategyA.getStrategy();
    }

    @Override
    public void algorithm() {
        System.out.println("process with strategyA...");
    }
}

