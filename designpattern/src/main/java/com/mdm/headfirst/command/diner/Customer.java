package com.mdm.headfirst.command.diner;

import com.mdm.headfirst.command.diner.Waitress;

public class Customer {
	Waitress waitress;
	Order order;
	public Customer(Waitress waitress) {
		this.waitress = waitress;
	}
	public void createOrder(Order order) {
		this.order = order;
	}
	public void hungry() {
		waitress.takeOrder(order);
	}
}