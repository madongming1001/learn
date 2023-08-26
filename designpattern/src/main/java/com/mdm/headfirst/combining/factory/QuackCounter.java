package com.mdm.headfirst.combining.factory;

public class QuackCounter implements Quackable {
    static int numberOfQuacks;
    Quackable duck;

    public QuackCounter(Quackable duck) {
        this.duck = duck;
    }

    public static int getQuacks() {
        return numberOfQuacks;
    }

    public void quack() {
        duck.quack();
        numberOfQuacks++;
    }

    public String toString() {
        return duck.toString();
    }
}
