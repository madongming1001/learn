package com.madm.learnroute.technology.spring;

import java.lang.reflect.Constructor;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 11:36 AM
 */
public class ThreadLocalHolder {

    private Long currentTime;

    /**
     * 不同业务设置不同的业务场景，如：业务A设置值为1，业务B设置值为2...
     */
    private static class InnerClass {
        private static final ThreadLocal<Integer> sceneThreadLocal = new ThreadLocal<>();
    }

    public ThreadLocalHolder() {
        initProperties();
    }

    private void initProperties() {
        this.currentTime = System.currentTimeMillis();
    }

    public static Integer getScene() {
        return InnerClass.sceneThreadLocal.get();
    }

    public static void initScene(Integer scene) {
        InnerClass.sceneThreadLocal.set(scene);
    }

    public static void clearScene() {
        initScene(null);
    }

    static class Singleton {
        private static volatile Singleton instance = null;

        private Singleton() {
        }

        public static Singleton getInstance() {        // 两层判空，第一层是为了避免不必要的同步
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {// 第二层是为了在null的情况下创建实例
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }

        public static void main(String[] args) throws Exception {
            Singleton instance = Singleton.getInstance();
            Constructor<Singleton> constructor = (Constructor<Singleton>) instance.getClass().getDeclaredConstructor();
            constructor.setAccessible(true);
            Singleton refInstance = constructor.newInstance();
            System.out.println(instance);
            System.out.println(refInstance);
            System.out.println(instance == refInstance);
        }
    }

    public enum SingletonEnum {
        INSTANCE;
        public void execute() {
            System.out.println("begin execute");
        }
    }
}
