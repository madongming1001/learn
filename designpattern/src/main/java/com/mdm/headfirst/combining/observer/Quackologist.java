package com.mdm.headfirst.combining.observer;

import com.mdm.headfirst.combining.observer.QuackObservable;

public class Quackologist implements Observer {
 
	public void update(QuackObservable duck) {
		System.out.println("Quackologist: " + duck + " just quacked.");
	}
 
	public String toString() {
		return "Quackologist";
	}
}
