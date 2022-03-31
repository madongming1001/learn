package com.madm.learnroute.spring;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 11:36 AM
 */
public class ThreadLocalHolder {

    private Long currentTime;

    /**
     * ·
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
}
