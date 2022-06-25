package com.mdm.headfirst.strategy;

import com.mdm.headfirst.strategy.FlyBehavior;

public class FlyNoWay implements FlyBehavior {
	public void fly() {
		System.out.println("I can't fly");
	}
}
