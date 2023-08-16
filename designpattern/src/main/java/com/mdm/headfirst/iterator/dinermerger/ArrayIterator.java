package com.mdm.headfirst.iterator.dinermerger;

public class ArrayIterator extends NodeIterator implements Iterator {
    public ArrayIterator(MenuItem[] items) {
        super.items = items;
    }

    public MenuItem next() {
        MenuItem menuItem = super.items[position];
        position = position + 1;
        return menuItem;
    }
}

class NodeIterator {
    MenuItem[] items;
    int position = 0;

    public boolean hasNext() {
        if (position >= items.length || items[position] == null) {
            return false;
        } else {
            return true;
        }
    }
}
