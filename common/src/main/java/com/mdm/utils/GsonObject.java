package com.mdm.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonObject {

    public static Gson createGson() {
        return new GsonBuilder()
                .serializeNulls()//默认情况下如果某一个属性为null，那么此属性不会参与序列化和反序列化的过程，加上此属性后会参与序列化和反序列化的过程
                .setPrettyPrinting()//格式化json字符串的输出，默认情况下是输出一行，经过这个属性设置后会格式化输出，即有缩进的输出
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//对时间进行格式化
                .create();
    }
}
