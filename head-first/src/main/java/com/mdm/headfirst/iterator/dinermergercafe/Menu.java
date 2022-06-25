package com.mdm.headfirst.iterator.dinermergercafe;

import com.mdm.headfirst.iterator.dinermergercafe.MenuItem;

import java.util.Iterator;

public interface Menu {
	public Iterator<MenuItem> createIterator();
}
