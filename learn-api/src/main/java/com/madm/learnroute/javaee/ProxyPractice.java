package com.madm.learnroute.javaee;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by YangTao
 */
interface Animal {
    void call();
}

class Cat implements Animal {
    @Override
    public void call() {
        System.out.println("喵喵喵 ~");
    }
}

class StaticProxyAnimal implements Animal {
    private Animal impl;
    public StaticProxyAnimal(Animal impl) {
        this.impl = impl;
    }
    @Override
    public void call() {
        System.out.println("猫饥饿");
        impl.call();
    }
}

class TargetInvoker implements InvocationHandler {
    // 代理中持有的目标类
    private Object target;

    public TargetInvoker(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk 代理执行前");
        Object result = method.invoke(target, args);
        System.out.println("jdk 代理执行后");
        return result;
    }
    public static Object getProxy(Object target) throws Exception {
        Object proxy = Proxy.newProxyInstance(
                // 指定目标类的类加载
                target.getClass().getClassLoader(),
                // 代理需要实现的接口，可指定多个，这是一个数组
                target.getClass().getInterfaces(),
                // 代理对象处理器
                new TargetInvoker(target)
        );
        return proxy;
    }
}

public class ProxyPractice {

    public static void main(String[] args) throws Exception {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        Cat cat = new Cat();
        Animal proxy = (Animal) TargetInvoker.getProxy(cat);
        proxy.call();

    }

}
