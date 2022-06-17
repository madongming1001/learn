package com.mdm.springfeature.enums;

import lombok.Getter;

/**
 * @Author: madongming
 * @DATE: 2022/6/8 10:54
 */
@Getter
public enum DataSourceEnum {
    BADGUY("badguyDataSource"), GOODBUY("goodbuyDataSource");

    private String name;

    DataSourceEnum(String name) {
        this.name = name;
    }
}
