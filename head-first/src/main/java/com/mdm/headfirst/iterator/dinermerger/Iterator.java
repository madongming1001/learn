package com.mdm.headfirst.iterator.dinermerger;

import com.mdm.headfirst.iterator.dinermerger.MenuItem;

public interface Iterator {
	boolean hasNext();
	MenuItem next();
}
