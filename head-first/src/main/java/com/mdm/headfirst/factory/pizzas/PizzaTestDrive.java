package com.mdm.headfirst.factory.pizzas;

import com.mdm.headfirst.factory.pizzas.SimplePizzaFactory;

public class PizzaTestDrive {
 
	public static void main(String[] args) {
		SimplePizzaFactory factory = new SimplePizzaFactory();
		PizzaStore store = new PizzaStore(factory);

		Pizza pizza = store.orderPizza("cheese");
		System.out.println("We ordered a " + pizza.getName() + "\n");
		System.out.println(pizza);
 
		pizza = store.orderPizza("veggie");
		System.out.println("We ordered a " + pizza.getName() + "\n");
		System.out.println(pizza);
	}
}
