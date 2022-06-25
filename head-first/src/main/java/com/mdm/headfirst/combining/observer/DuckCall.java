package com.mdm.headfirst.combining.observer;

import com.mdm.headfirst.combining.observer.Observable;
import com.mdm.headfirst.combining.observer.Observer;
import com.mdm.headfirst.combining.observer.Quackable;

public class DuckCall implements Quackable {
	Observable observable;

	public DuckCall() {
		observable = new Observable(this);
	}
 
	public void quack() {
		System.out.println("Kwak");
		notifyObservers();
	}
 
	public void registerObserver(Observer observer) {
		observable.registerObserver(observer);
	}

	public void notifyObservers() {
		observable.notifyObservers();
	}
 
	public String toString() {
		return "Duck Call";
	}
}
