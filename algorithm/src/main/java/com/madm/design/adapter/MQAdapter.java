package com.madm.design.adapter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public class MQAdapter {

    private static ThreadLocal<SimpleDateFormat> tlsf = new ThreadLocal<SimpleDateFormat>() {{
        set(new SimpleDateFormat("yyyy-MM-dd"));
    }};

    public static RebateInfo filter(String strJson, Map<String, String> link) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return filter(JSON.parseObject(strJson, Map.class), link);
    }

    public static RebateInfo filter(Map obj, Map<String, String> link) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RebateInfo rebateInfo = new RebateInfo();
        for (String key : link.keySet()) {
            Object val = obj.get(link.get(key));
            Class parameterTypes = String.class;
            if (val instanceof Long && val.toString().length() == 13) {
                val = new Date((Long) val);
                parameterTypes = Date.class;
            }
            RebateInfo.class.getMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), parameterTypes).invoke(rebateInfo, val);
        }
        return rebateInfo;
    }
}
