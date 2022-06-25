package com.mdm.headfirst.combining.adapter;

import com.mdm.headfirst.combining.adapter.Quackable;

public class DecoyDuck implements Quackable {
	public void quack() {
		System.out.println("<< Silence >>");
	}
}
