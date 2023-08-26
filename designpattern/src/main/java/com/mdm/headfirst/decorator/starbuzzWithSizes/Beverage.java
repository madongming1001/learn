package com.mdm.headfirst.decorator.starbuzzWithSizes;

public abstract class Beverage {
    Size size = Size.TALL;

    ;
    String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }

    public Size getSize() {
        return this.size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public abstract double cost();

    public enum Size {TALL, GRANDE, VENTI}
}
