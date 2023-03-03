package com.madm.learnroute.enums;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.madm.learnroute.technology.disruptor.BeanManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongming.ma
 * @date 2023/3/3 16:03
 */
@Getter
@AllArgsConstructor
public enum BookPurchaseRecordEnum {
    BUYER(1, "accountServiceImpl"),
    BOOK(2, "bookServiceImpl,bookStockServiceImpl");
    private Integer code;
    private String className;

    public void execute(Integer code) {
        for (BookPurchaseRecordEnum element : values()) {
            if (element.getCode() == code) {
                for (String className : element.className.split(StringPool.COMMA)) {
                    BeanManager.getHandler(className).findAccountById(1);
                }
            }
        }
    }
}
