package com.mdm.headfirst.observer.simpleobservable;

import com.mdm.headfirst.observer.simpleobservable.SimpleObserver;
import com.mdm.headfirst.observer.simpleobservable.SimpleSubject;

public class Example {

	public static void main(String[] args) {
		SimpleSubject simpleSubject = new SimpleSubject();
	
		SimpleObserver simpleObserver = new SimpleObserver(simpleSubject);

		simpleSubject.setValue(80);
	}
}
