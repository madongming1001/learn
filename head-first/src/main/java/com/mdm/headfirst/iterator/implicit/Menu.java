package com.mdm.headfirst.iterator.implicit;

import com.mdm.headfirst.iterator.implicit.MenuItem;

import java.util.Iterator;

public interface Menu {
	public Iterator<MenuItem> createIterator();
}
