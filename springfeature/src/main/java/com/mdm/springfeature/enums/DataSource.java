package com.mdm.springfeature.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: madongming
 * @DATE: 2022/6/8 10:54
 */
@Getter
@AllArgsConstructor
public enum DataSource {
    BADGUY(DataSourceType.BADGUY),
    GOODBUY(DataSourceType.GOODBUY);

    private final String name;
}
