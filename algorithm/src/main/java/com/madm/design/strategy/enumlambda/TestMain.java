package com.madm.design.strategy.enumlambda;

/**
 * 策略模式简化写法 来优化if else逻辑
 */
public class TestMain {


    public static void main(String[] args) {
        String low_risk = returnHome("LOW_RISK", 1);
        System.out.println(low_risk);

    }

    /**
     * 某人回村
     * @param from 代表区域风险系数
     * @param id   打工人
     * @return 要证明
     */
    public static String returnHome(String from, Integer id) {
        for (ReturnHomeStrategy value : ReturnHomeStrategy.values()) {
            if (value.predicate().test(from)) {
                return value.function().apply(id);
            }
        }
        throw new RuntimeException("外星人,抓起来放进动物园卖门票!");
    }
}
