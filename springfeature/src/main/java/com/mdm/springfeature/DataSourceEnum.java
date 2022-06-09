package com.mdm.springfeature;

import lombok.Data;

/**
 * @Author: madongming
 * @DATE: 2022/6/8 10:54
 */
public enum DataSourceEnum {
    BADGUY("badguyDataSource"),
    GOODBUY("goodbuyDataSource");

    private String name;

    DataSourceEnum(String name) {
        this.name = name;
    }
}
