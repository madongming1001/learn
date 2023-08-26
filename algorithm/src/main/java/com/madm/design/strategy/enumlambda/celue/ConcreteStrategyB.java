package com.madm.design.strategy.enumlambda.celue;

public class ConcreteStrategyB implements Strategy {

    @Override
    public String strategy() {
        return StrategySelector.strategyB.getStrategy();
    }

    @Override
    public void algorithm() {
        System.out.println("process with strategyB...");
    }
}

