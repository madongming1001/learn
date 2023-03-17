package com.madm.design.strategy.classmethod;

/**
 * 查询购物卷的派发方式
 */
public class ShoppingStrategy implements Strategy {


    @Override
    public String query(String resourceId) {
        return "每周三20点发放";
    }
}
