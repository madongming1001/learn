package com.mdm.headfirst.combining.observer;

import com.mdm.headfirst.combining.observer.QuackObservable;

public interface Observer {
	public void update(QuackObservable duck);
}
