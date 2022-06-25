package com.mdm.headfirst.decorator.starbuzzWithSizes;

import com.mdm.headfirst.decorator.starbuzzWithSizes.Beverage;
import com.mdm.headfirst.decorator.starbuzzWithSizes.CondimentDecorator;

public class Milk extends CondimentDecorator {
	public Milk(Beverage beverage) {
		this.beverage = beverage;
	}

	public String getDescription() {
		return beverage.getDescription() + ", Milk";
	}

	public double cost() {
		return beverage.cost() + .10;
	}
}
