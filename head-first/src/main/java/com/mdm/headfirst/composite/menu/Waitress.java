package com.mdm.headfirst.composite.menu;

import com.mdm.headfirst.composite.menu.MenuComponent;

public class Waitress {
	MenuComponent allMenus;
 
	public Waitress(MenuComponent allMenus) {
		this.allMenus = allMenus;
	}
 
	public void printMenu() {
		allMenus.print();
	}
}
