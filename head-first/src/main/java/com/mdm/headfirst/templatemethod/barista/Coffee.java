package com.mdm.headfirst.templatemethod.barista;

import com.mdm.headfirst.templatemethod.barista.CaffeineBeverage;

public class Coffee extends CaffeineBeverage {
	public void brew() {
		System.out.println("Dripping Coffee through filter");
	}
	public void addCondiments() {
		System.out.println("Adding Sugar and Milk");
	}
}
