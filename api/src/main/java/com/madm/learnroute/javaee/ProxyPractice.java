package com.madm.learnroute.javaee;


import com.madm.learnroute.annotation.UserId;
import lombok.SneakyThrows;
import net.sf.cglib.core.DebuggingClassWriter;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cglib.proxy.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * 代理模式是一种设计模式，提供了对目标对象额外的访问方式，
 * 即通过代理对象访问目标对象，这样可以在不修改原目标对象的前提下，
 * 提供额外的功能操作，扩展目标对象的功能。
 * jdk代理对象的缺点：被代理对象必须实现一个或多个接口
 * Cglib代理对象的缺点：目标不能是final类
 * Created by YangTao
 * CallbackInfo 里面有很多回掉的类型
 *     private static final CallbackInfo[] CALLBACKS = {
 *         new CallbackInfo(NoOp.class, NoOpGenerator.INSTANCE),
 *         new CallbackInfo(MethodInterceptor.class, MethodInterceptorGenerator.INSTANCE),
 *         new CallbackInfo(InvocationHandler.class, InvocationHandlerGenerator.INSTANCE),
 *         new CallbackInfo(LazyLoader.class, LazyLoaderGenerator.INSTANCE),
 *         new CallbackInfo(Dispatcher.class, DispatcherGenerator.INSTANCE),
 *         new CallbackInfo(FixedValue.class, FixedValueGenerator.INSTANCE),
 *         new CallbackInfo(ProxyRefDispatcher.class, DispatcherGenerator.PROXY_REF_INSTANCE),
 *     };
 *
 */
interface Animal {
    void call();

    void eat();
}

class Cat implements Animal {
    @UserId("sdfd")
    public void call() {
        System.out.println("喵喵喵 ~");
    }

    public void eat() {
        System.out.println("吃东西 ～");
    }
}

class Dog implements Animal {
    public void call() {
        System.out.println("汪汪汪 ~");
    }

    public void eat() {
        System.out.println("呃呃呃 ～");
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
        System.out.println(proxy.getClass());
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
                new TargetInvoker(target));
        return proxy;
    }
}

public class ProxyPractice {

    private static void cglibProxyGenerate() {
        saveGeneratedCGlibProxyFile(System.getProperty("user.dir") + "/proxy");
        //DebuggingClassWriter.traceEnabled 打印cdlib代理文件
        //通过cglib动态代理获取代理对象的过程，创建调用的对象，在后续创建过程中Enhancekey的对象，
        //所以在进行enhancer对象创建的时候需要把EnhancerKey（newInstance）对象准备好
        //恰好这个对象也需要动态代理生成
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Cat.class);
        //obj是代理子类实例，method是父类方法，args是方法如入参，proxy是代理子类方法，用于委托基类中的原方法。
        enhancer.setCallbacks(new Callback[]{(MethodInterceptor) (obj, method, args, proxy) -> {
            System.out.println("before");
            Object result = proxy.invokeSuper(obj, args);//不报错
//            Object result = method.invoke(new Cat(), args);
//            Object result = proxy.invoke(obj, args);//报错 由于obj是创建的代理对象 他还是会调用代理的方法 所以递归循环了 args 是参数
            System.out.println("after");
            return result;
        }, NoOp.INSTANCE});
        enhancer.setCallbackFilter(method -> 0);


        Cat cat = (Cat) enhancer.create();
        cat.call();
    }

    private static void jdkProxyGenerate() throws Exception {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");//jdk 1.8
//        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");//jdk 1.8 after
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

    public static void main(String[] args) throws Exception {
        //JDK代理生成 InvocationHandler
//        jdkProxyGenerate();

        //CGLIB代理生成 MethodInterceptor
//        cglibProxyGenerate();

        testReflectMethod();
    }

    /**
     * 测试从一个对象获取method对象，穿参数为另一个对象，测试方法能否调用成功
     */
    @SneakyThrows
    private static void testReflectMethod() {
        Class<Cat> catClass = Cat.class;
        Class<?> declaringClass = catClass.getDeclaringClass();
        Method[] methods1 = catClass.getMethods();
        System.out.println(declaringClass);
        Method[] methods = catClass.getDeclaredMethods();
        for (Method method : methods) {
//            Dog cat = new Dog(); cause：object is not an instance of declaring class
            Cat cat = new Cat();
            method.invoke(cat);
            UserId a = method.getAnnotation(UserId.class);
            System.out.println(a.value());
        }

    }
}
