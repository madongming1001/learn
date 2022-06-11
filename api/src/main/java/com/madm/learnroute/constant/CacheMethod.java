package com.madm.learnroute.constant;

public enum CacheMethod {

    /**
     * 获取菜单
     */
    GET_MENU(CacheConstants.GET_MENU, CacheConstants.EXPIRES_5_MIN),
    /**
     * 获取菜单列表
     */
    GET_MENU_LIST(CacheConstants.GET_MENU_LIST, CacheConstants.EXPIRES_10_MIN);

    /**
     * 缓存名称
     */
    private final String name;
    /**
     * 过期时间
     */
    private final int expires;

    /**
     * 构造
     */
    CacheMethod(String name, int expires) {
        this.name = name;
        this.expires = expires;
    }

    public String getName() {
        return name;
    }

    public int getExpires() {
        return expires;
    }
}

