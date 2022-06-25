package com.mdm.headfirst.combining.composite;

import com.mdm.headfirst.combining.composite.AbstractDuckFactory;
import com.mdm.headfirst.combining.composite.MallardDuck;
import com.mdm.headfirst.combining.composite.RedheadDuck;

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
