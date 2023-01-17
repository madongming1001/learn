package com.madm.learnroute.constant;

import lombok.Getter;

/**
 * @author dongming.ma
 * @date 2023/1/17 15:11
 */
@Getter
public enum TraceLogEnum {

    TRACE_LOG_ID("TRACE_LOG_ID");
    private int id;
    private String value;

    TraceLogEnum(String value) {
        this.value = value;
    }

    TraceLogEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public static TraceLogEnum getById(int id) {
        for (TraceLogEnum item : TraceLogEnum.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
