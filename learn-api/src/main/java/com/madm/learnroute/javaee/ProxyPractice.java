package com.madm.learnroute.javaee;


import net.sf.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

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

        //JDK代理生成
        jdkProxyGenerate();

        //CGLIB代理生成
		cglibProxyGenerate();
    }

    private static void cglibProxyGenerate() {
        saveGeneratedCGlibProxyFile(System.getProperty("user.dir") + "/proxy");
        //通过cglib动态代理获取代理对象的过程，创建调用的对象，在后续创建过程中Enhancekey的对象，所以在进行enhancer对象创建的时候需要把EnhancerKey（newInstance）对象准备好
        //恰好这个对象也需要动态代理生成
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Cat.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o, objects);
            }
        });
        Cat cat = (Cat) enhancer.create();
        cat.call();
    }

    private static void jdkProxyGenerate() throws Exception {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        Cat cat = new Cat();
        Animal proxy = (Animal) TargetInvoker.getProxy(cat);
        proxy.call();
    }

    public static void saveGeneratedCGlibProxyFile(String dir) {
        try {
            Field field = System.class.getDeclaredField("props");
            field.setAccessible(true);
            Properties properties = (Properties) field.get(null);
            System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, dir);
            properties.put("net.sf.cglib.core.DebuggingClassWriter.traceEnabled", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
