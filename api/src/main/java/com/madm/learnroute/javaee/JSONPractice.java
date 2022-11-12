package com.madm.learnroute.javaee;

import com.alibaba.fastjson.JSON;
import com.mdm.pojo.User;

/**
 * @author dongming.ma
 * @date 2022/11/13 10:35
 */
public class JSONPractice {
    public static void main(String[] args) {
        User user = new User(1);
        String json = JSON.toJSONString(user);
        System.out.printf(json);
    }
}
