package com.mdm.headfirst.strategy;

import com.mdm.headfirst.strategy.QuackBehavior;

public class Squeak implements QuackBehavior {
	public void quack() {
		System.out.println("Squeak");
	}
}
