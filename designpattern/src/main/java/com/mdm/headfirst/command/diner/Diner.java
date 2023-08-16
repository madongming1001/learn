package com.mdm.headfirst.command.diner;

import com.mdm.headfirst.command.diner.BurgerAndFriesOrder;
import com.mdm.headfirst.command.diner.Customer;
import com.mdm.headfirst.command.diner.Waitress;

public class Diner {
	public static void main(String[] args) {
		Cook cook = new Cook();
		Waitress waitress = new Waitress();
		Customer customer = new Customer(waitress);
		customer.createOrder(new BurgerAndFriesOrder(cook));
		customer.hungry();
	}
}