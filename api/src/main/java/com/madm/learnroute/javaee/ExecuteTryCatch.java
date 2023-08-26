package com.madm.learnroute.javaee;

import com.alibaba.fastjson.JSONObject;

import java.net.URLDecoder;
import java.util.Objects;

/**
 * @author dongming.ma
 * @date 2023/2/22 19:08
 */
public class ExecuteTryCatch {
    public static void main(String[] args) {
        int times = 1000000;// 百万次
        //加了校验效率翻倍
        long nao1 = System.nanoTime();
        ExecuteTryCatch executeTryCatch = new ExecuteTryCatch();
        for (int i = 0; i < times; i++) {
            executeTryCatch.getThenAddWithJudge(new JSONObject(), "anyKey");
        }
        long end1 = System.nanoTime();
        System.out.println("未抛出异常耗时：millions=" + (end1 - nao1) / 1000000 + "毫秒 nao=" + (end1 - nao1) + "微秒");

        //不加校验每次都得goto跳转异常catch效率地下
        long nao2 = System.nanoTime();
        for (int i = 0; i < times; i++) {
            executeTryCatch.getThenAddNoJudge(new JSONObject(), "anyKey");
        }
        long end2 = System.nanoTime();
        System.out.println("每次必抛出异常：millions=" + (end2 - nao2) / 1000000 + "毫秒 nao=" + (end2 - nao2) + "微秒");
    }

    private int getThenAddNoJudge(JSONObject json, String key) {
        if (Objects.isNull(json)) {
            throw new IllegalArgumentException("参数异常");
        }
        int num;
        try {
            // 不校验 key 是否未空值，直接调用 toString 每次触发空指针异常并被捕获
            num = 100 + Integer.parseInt(URLDecoder.decode(json.get(key).toString(), "UTF-8"));
        } catch (Exception e) {
            num = 100;
        }
        return num;
    }

    private int getThenAddWithJudge(JSONObject json, String key) {
        if (Objects.isNull(json)) {
            throw new IllegalArgumentException("参数异常");
        }
        int num;
        try {
            // 校验 key 是否未空值
            num = 100 + Integer.parseInt(URLDecoder.decode(Objects.toString(json.get(key), "0"), "UTF-8"));
        } catch (Exception e) {
            num = 100;
        }
        return num;

    }
}
