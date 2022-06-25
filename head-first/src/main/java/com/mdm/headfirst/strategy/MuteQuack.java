package com.mdm.headfirst.strategy;

import com.mdm.headfirst.strategy.QuackBehavior;

public class MuteQuack implements QuackBehavior {
	public void quack() {
		System.out.println("<< Silence >>");
	}
}
