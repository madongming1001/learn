package com.mdm.headfirst.combining.observer;

import com.mdm.headfirst.combining.observer.AbstractDuckFactory;
import com.mdm.headfirst.combining.observer.MallardDuck;
import com.mdm.headfirst.combining.observer.RedheadDuck;

public class DuckFactory extends AbstractDuckFactory {
  
	public Quackable createMallardDuck() {
		return new MallardDuck();
	}
  
	public Quackable createRedheadDuck() {
		return new RedheadDuck();
	}
  
	public Quackable createDuckCall() {
		return new DuckCall();
	}
   
	public Quackable createRubberDuck() {
		return new RubberDuck();
	}
}
