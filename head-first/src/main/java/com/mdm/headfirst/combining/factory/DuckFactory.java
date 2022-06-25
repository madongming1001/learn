package com.mdm.headfirst.combining.factory;

import com.mdm.headfirst.combining.factory.AbstractDuckFactory;
import com.mdm.headfirst.combining.factory.MallardDuck;
import com.mdm.headfirst.combining.factory.RedheadDuck;

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
