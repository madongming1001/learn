package com.mdm.headfirst.combining.composite;

import com.mdm.headfirst.combining.composite.Quackable;

public class DecoyDuck implements Quackable {
 
	public void quack() {
		System.out.println("<< Silence >>");
	}
 
	public String toString() {
		return "Decoy Duck";
	}
}
