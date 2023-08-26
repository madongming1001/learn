package com.mdm.headfirst.decorator.pizza;

public abstract class ToppingDecorator extends Pizza {
    Pizza pizza;

    public abstract String getDescription();
}
