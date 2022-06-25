package com.mdm.headfirst.decorator.starbuzz;

import com.mdm.headfirst.decorator.starbuzz.Beverage;

public class Decaf extends Beverage {
	public Decaf() {
		description = "Decaf Coffee";
	}
 
	public double cost() {
		return 1.05;
	}
}

