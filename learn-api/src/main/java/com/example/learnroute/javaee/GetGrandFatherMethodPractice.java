package com.example.learnroute.javaee;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

class GrandFather {
    public void thinking() {
        System.out.println("GrandFather thinking");
    }
}

class Father extends GrandFather {
    public void thinking() {
        System.out.println("Father thinking");
    }
}

class Son extends Father {
    public void thinking() {
        try {
//            MethodType mt = MethodType.methodType(void.class);
//            MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
//            mh.invoke(this);
            MethodType mt = MethodType.methodType(void.class);
            Field lookupImpl = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            lookupImpl.setAccessible(true);
            MethodHandles.Lookup lookup = (MethodHandles.Lookup) lookupImpl.get(null);
            MethodHandle mh = lookup.findSpecial(GrandFather.class,"thinking",mt,GrandFather.class);
            mh.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

public class GetGrandFatherMethodPractice {
    public static void main(String[] args) {
        Son son = new Son();
        son.thinking();
    }
}
