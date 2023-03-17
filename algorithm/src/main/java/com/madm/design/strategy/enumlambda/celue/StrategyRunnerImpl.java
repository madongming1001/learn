package com.madm.design.strategy.enumlambda.celue;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrategyRunnerImpl implements StrategyRunner {

    private static final List<Strategy> STRATEGIES = Arrays.asList(new ConcreteStrategyA(), new ConcreteStrategyB(), new ConcreteStrategyC());
    private static Map<String, Strategy> STRATEGY_MAP = new HashMap<>();

    static {
        STRATEGY_MAP = STRATEGIES.stream().collect(Collectors.toMap(Strategy::strategy, s -> s));
    }

    @Override
    public void execute(String strategy) {
        STRATEGY_MAP.get(strategy).algorithm();
    }
}
