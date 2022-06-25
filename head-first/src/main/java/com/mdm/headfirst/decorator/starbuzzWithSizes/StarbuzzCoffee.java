package com.mdm.headfirst.decorator.starbuzzWithSizes;

import com.mdm.headfirst.decorator.starbuzzWithSizes.Beverage.Size;

public class StarbuzzCoffee {
 
	public static void main(String args[]) {
//		Beverage beverage = new Espresso();
//		System.out.println(beverage.getDescription()
//				+ " $" + String.format("%.2f", beverage.cost()));
 
		Beverage beverage2 = new DarkRoast();//.99
		beverage2 = new Mocha(beverage2);//.20
		beverage2 = new Mocha(beverage2);//.20
		beverage2 = new Whip(beverage2);//.10
		System.out.println(beverage2.getDescription() 
				+ " $" + String.format("%.2f", beverage2.cost()));
 
//		Beverage beverage3 = new HouseBlend();
//		beverage3.setSize(Size.VENTI);
//		beverage3 = new Soy(beverage3);
//		beverage3 = new Mocha(beverage3);
//		beverage3 = new Whip(beverage3);
//		System.out.println(beverage3.getDescription()
//				+ " $" + String.format("%.2f", beverage3.cost()));
	}
}
