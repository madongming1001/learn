package com.mdm.headfirst.iterator.dinermergeri;

import com.mdm.headfirst.iterator.dinermergeri.MenuItem;

import java.util.Iterator;

public interface Menu {
	public Iterator<MenuItem> createIterator();
}
