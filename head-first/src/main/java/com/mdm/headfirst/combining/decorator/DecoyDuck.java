package com.mdm.headfirst.combining.decorator;

import com.mdm.headfirst.combining.decorator.Quackable;

public class DecoyDuck implements Quackable {
 
	public void quack() {
		System.out.println("<< Silence >>");
	}
 
	public String toString() {
		return "Decoy Duck";
	}
}
