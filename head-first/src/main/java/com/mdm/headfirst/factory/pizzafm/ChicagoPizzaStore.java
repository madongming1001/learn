package com.mdm.headfirst.factory.pizzafm;

import com.mdm.headfirst.factory.pizzafm.ChicagoStyleCheesePizza;
import com.mdm.headfirst.factory.pizzafm.ChicagoStyleClamPizza;
import com.mdm.headfirst.factory.pizzafm.ChicagoStylePepperoniPizza;
import com.mdm.headfirst.factory.pizzafm.ChicagoStyleVeggiePizza;

public class ChicagoPizzaStore extends PizzaStore {

	Pizza createPizza(String item) {
        	if (item.equals("cheese")) {
            		return new ChicagoStyleCheesePizza();
        	} else if (item.equals("veggie")) {
        	    	return new ChicagoStyleVeggiePizza();
        	} else if (item.equals("clam")) {
        	    	return new ChicagoStyleClamPizza();
        	} else if (item.equals("pepperoni")) {
            		return new ChicagoStylePepperoniPizza();
        	} else return null;
	}
}
