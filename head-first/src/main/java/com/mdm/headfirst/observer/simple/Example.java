package com.mdm.headfirst.observer.simple;

import com.mdm.headfirst.observer.simple.SimpleObserver;
import com.mdm.headfirst.observer.simple.SimpleSubject;

public class Example {

	public static void main(String[] args) {
		SimpleSubject simpleSubject = new SimpleSubject();
	
		SimpleObserver simpleObserver = new SimpleObserver(simpleSubject);

		simpleSubject.setValue(80);
		
		simpleSubject.removeObserver(simpleObserver);
	}
}
