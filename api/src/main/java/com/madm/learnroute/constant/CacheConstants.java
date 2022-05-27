package com.madm.learnroute.constant;

public class CacheConstants {
    /**
     * 默认过期时间（配置类中我使用的时间单位是秒，所以这里如 3*60 为3分钟）
     */
    public static final int DEFAULT_EXPIRES = 3 * 60;
    public static final int EXPIRES_5_MIN = 5 * 60;
    public static final int EXPIRES_10_MIN = 10 * 60;

    public static final String GET_MENU = "GET:MENU:";
    public static final String GET_MENU_LIST = "GET:MENU:LIST:";
}

