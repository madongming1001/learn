package com.mdm.headfirst.observer.simple;

import java.util.ArrayList;
import java.util.List;

public class SimpleSubject implements Subject {
    private List<com.mdm.headfirst.observer.simple.Observer> observers;
    private int value = 0;

    public SimpleSubject() {
        observers = new ArrayList();
    }

    @Override
    public void registerObserver(com.mdm.headfirst.observer.simple.Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(com.mdm.headfirst.observer.simple.Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(value);
        }
    }

    public void setValue(int value) {
        this.value = value;
        notifyObservers();
    }
}