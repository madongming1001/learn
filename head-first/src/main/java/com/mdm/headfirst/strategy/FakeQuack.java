package com.mdm.headfirst.strategy;

import com.mdm.headfirst.strategy.QuackBehavior;

public class FakeQuack implements QuackBehavior {
	public void quack() {
		System.out.println("Qwak");
	}
}
