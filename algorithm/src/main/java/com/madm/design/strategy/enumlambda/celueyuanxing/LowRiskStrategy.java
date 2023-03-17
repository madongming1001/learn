package com.madm.design.strategy.enumlambda.celueyuanxing;

public class LowRiskStrategy implements FunctionStrategy {


    @Override
    public void huicun() {
        System.out.println("在低风险回来了，先做个核酸吧!");
    }
}
