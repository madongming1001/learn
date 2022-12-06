package com.madm.learnroute.javaee;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Super_A superA = getA();
        System.out.println(superA.getA());
        superA.setA(new Field_Sub(4));
        System.out.println(superA.getA());

    }

    public static Super_A getA() {
        return new Sub_B();
    }
}

class Super_A {
//    private Field_Super a = new Field_Super(1);
    private Field_Sub a = new Field_Sub(1);

    public Field_Sub getA() {
        return a;
    }

    public void setA(Field_Sub a) {
        this.a = a;
    }
}

class Sub_B extends Super_A {
    private Field_Sub a = new Field_Sub(3);

    @Override
    public Field_Sub getA() {
        return a;
    }

    @Override
    public void setA(Field_Sub a) {
        this.a = a;
    }
}


class Field_Super {
    private int a;

    public Field_Super(int a) {
        this.a = a;
    }

    public Field_Super() {
    }

    @Override
    public String toString() {
        return "ArgSuper: " + a;
    }
}

class Field_Sub extends Field_Super {
    private int a;

    public Field_Sub(int a) {
        super();
        this.a = a;
    }

    @Override
    public String toString() {
        return "ArgSub: " + a;
    }
}