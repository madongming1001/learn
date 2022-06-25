package com.mdm.headfirst.strategy;

import com.mdm.headfirst.strategy.QuackBehavior;

public class Quack implements QuackBehavior {
	public void quack() {
		System.out.println("Quack");
	}
}
