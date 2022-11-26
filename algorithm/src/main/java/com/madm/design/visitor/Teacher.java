package com.madm.design.visitor;

import java.math.BigDecimal;

public class Teacher extends User {
    public Teacher(String name, String identity, String clazz) {
        super(name, identity, clazz);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public double entranceRatio() {
        return BigDecimal.valueOf(Math.random() * 100).setScale(2,
                BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
