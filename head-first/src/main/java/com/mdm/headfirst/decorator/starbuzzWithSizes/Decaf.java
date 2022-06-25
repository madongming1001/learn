package com.mdm.headfirst.decorator.starbuzzWithSizes;

import com.mdm.headfirst.decorator.starbuzzWithSizes.Beverage;

public class Decaf extends Beverage {
	public Decaf() {
		description = "Decaf Coffee";
	}
 
	public double cost() {
		return 1.05;
	}
}

