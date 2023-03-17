package com.madm.design.strategy.classmethod;

/**
 * 还在用策略模式解决 if-else？
 * Map + 函数式接口来帮你搞定！
 *
 * 根据优惠券类型resourceType -> 确定查询哪个数据表
 * 根据编码resourceId -> 到对应的数据表里边查询优惠券的派发方式
 *
 * 优惠券有多种类型，分别对应了不同的数据库表：
 * 红包 —— 红包发放规则表
 * 购物券 —— 购物券表
 * ...
 */
public class CeLueTest {

    public static void main(String[] args) {
        String resourceType = "";
        String resourceId = "";
        switch (resourceType) {
            case "红包":
//                查询红包的派发方式
                break;
            case "购物券":
//                查询购物券的派发方式
                break;
            default:
                System.out.println("查找不到该优惠券类型resourceType以及对应的派发方式");
                break;
        }

//策略模式优化之后
        String grantType = "";
        switch (resourceType) {
            case "红包":
//                查询红包的派发方式
                grantType = new Content(new RedPaperStrategy()).contentStrategy(resourceId);
                break;
            case "购物券":
//                查询购物券的派发方式 
                grantType = new Content(new ShoppingStrategy()).contentStrategy(resourceId);
                break;
            default:
                System.out.println("查找不到该优惠券类型resourceType以及对应的派发方式");
                break;
        }


    }
}
