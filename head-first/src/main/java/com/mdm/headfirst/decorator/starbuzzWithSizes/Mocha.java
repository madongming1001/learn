package com.mdm.headfirst.decorator.starbuzzWithSizes;

import com.mdm.headfirst.decorator.starbuzzWithSizes.Beverage;
import com.mdm.headfirst.decorator.starbuzzWithSizes.CondimentDecorator;

public class Mocha extends CondimentDecorator {
	public Mocha(Beverage beverage) {
		this.beverage = beverage;
	}
 
	public String getDescription() {
		return beverage.getDescription() + ", Mocha";
	}
 
	public double cost() {
		return beverage.cost() + .20;
	}
}
