package com.mdm.headfirst.templatemethod.barista;

import com.mdm.headfirst.templatemethod.barista.CaffeineBeverage;

public class Tea extends CaffeineBeverage {
	public void brew() {
		System.out.println("Steeping the tea");
	}
	public void addCondiments() {
		System.out.println("Adding Lemon");
	}
}
