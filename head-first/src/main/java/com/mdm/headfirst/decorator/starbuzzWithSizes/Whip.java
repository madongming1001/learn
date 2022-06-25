package com.mdm.headfirst.decorator.starbuzzWithSizes;

import com.mdm.headfirst.decorator.starbuzzWithSizes.Beverage;
import com.mdm.headfirst.decorator.starbuzzWithSizes.CondimentDecorator;

public class Whip extends CondimentDecorator {
	public Whip(Beverage beverage) {
		this.beverage = beverage;
	}
 
	public String getDescription() {
		return beverage.getDescription() + ", Whip";
	}
 
	public double cost() {
		return beverage.cost() + .10;
	}
}
