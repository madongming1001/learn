package com.mdm.headfirst.combining.factory;

import com.mdm.headfirst.combining.factory.Quackable;

public class DecoyDuck implements Quackable {
 
	public void quack() {
		System.out.println("<< Silence >>");
	}
 
	public String toString() {
		return "Decoy Duck";
	}
}
