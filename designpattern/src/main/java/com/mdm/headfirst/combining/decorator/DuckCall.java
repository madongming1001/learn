package com.mdm.headfirst.combining.decorator;

import com.mdm.headfirst.combining.decorator.Quackable;

public class DuckCall implements Quackable {
 
	public void quack() {
		System.out.println("Kwak");
	}
 
	public String toString() {
		return "Duck Call";
	}
}
