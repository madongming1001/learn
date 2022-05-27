package com.madm.learnroute.reptile;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {

        Map<String, Map<String, String>> map = new HashMap<>();

        JsoupPractice.parseProvinceName(map, "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018");

        System.out.println(JSON.toJSONString(map));
    }
}
