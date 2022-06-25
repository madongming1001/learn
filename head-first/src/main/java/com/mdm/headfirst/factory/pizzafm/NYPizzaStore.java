package com.mdm.headfirst.factory.pizzafm;

import com.mdm.headfirst.factory.pizzafm.NYStyleCheesePizza;
import com.mdm.headfirst.factory.pizzafm.NYStyleClamPizza;
import com.mdm.headfirst.factory.pizzafm.NYStylePepperoniPizza;
import com.mdm.headfirst.factory.pizzafm.NYStyleVeggiePizza;

public class NYPizzaStore extends PizzaStore {

	Pizza createPizza(String item) {
		if (item.equals("cheese")) {
			return new NYStyleCheesePizza();
		} else if (item.equals("veggie")) {
			return new NYStyleVeggiePizza();
		} else if (item.equals("clam")) {
			return new NYStyleClamPizza();
		} else if (item.equals("pepperoni")) {
			return new NYStylePepperoniPizza();
		} else return null;
	}
}
