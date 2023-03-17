package com.madm.design.strategy.classmethod;

/**
 * 查询红包的派发方式
 */
public class RedPaperStrategy implements Strategy {


    @Override
    public String query(String resourceId) {
        return "每周末9点发放";
    }
}
