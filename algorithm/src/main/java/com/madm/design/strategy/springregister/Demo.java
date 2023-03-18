package com.madm.design.strategy.springregister;

/**
 * 用策略模式和工厂模式以及模板方法模式优化代码中的if else
 */
public class Demo {
    public static void main(String[] args) {
        String nickName = "蜗牛";
        if ("皮球".equals(nickName)) {
            //业务逻辑
//            System.out.println("我是皮球");
            new PiQiuHandler().AA("皮球");
        }else  if ("蜗牛".equals(nickName)){
            //业务逻辑
//            System.out.println("我是蜗牛");
            new WoNiuHandler().AA("蜗牛");
        }else  if ("牛蛙".equals(nickName)){
            //业务逻辑
            System.out.println("我是牛蛙");
            System.out.println("呱呱呱");
        }

        String nickName2 = "woniu";
//        Handler invokeStrategy = Factory.getInvokeStrategy(nickName2);
//        invokeStrategy.AA(nickName2);

        AbstractHandler invokeStrategy = Factory2.getInvokeStrategy(nickName2);
        invokeStrategy.AA(nickName2);

        invokeStrategy.BB(nickName2);
    }

    private static void extracted() {
        String nickName = "woniu";
        AbstractHandler invokeStrategy = Factory2.getInvokeStrategy(nickName);

        invokeStrategy.AA(nickName);

        invokeStrategy.BB(nickName);


    }


}
