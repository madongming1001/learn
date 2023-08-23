package com.madm.learnroute.javaee;

/**
 * @author dongming.ma
 * @date 2023/8/23 13:32
 */
public class OuterClassPractice {
    private int outerVariable;

    public void outerMethod() { // 创建内部类实例
        InnerClass inner = new InnerClass();
        // 调用内部类的方法
        inner.innerMethod();
    }

    public class InnerClass {
        public void innerMethod() {
            // 在这里写内部类的方法逻辑
            System.out.println("Inner method called");
        }
    }

    public static void main(String[] args) {
        // 创建外部类实例
        OuterClassPractice outer = new OuterClassPractice();
        // 调用外部类的方法，间接调用内部类的方法
        outer.outerMethod();
    }
}



