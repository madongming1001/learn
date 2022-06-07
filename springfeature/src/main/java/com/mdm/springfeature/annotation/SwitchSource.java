package com.mdm.springfeature.annotation;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 17:20
 */

import java.lang.annotation.*;

/**
 * 切换数据源的注解
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface SwitchSource {

    /**
     * 默认切换的数据源KEY
     */
    String DEFAULT_NAME = "badguyDataSource";

    /**
     * 需要切换到数据的KEY
     */
    String value() default DEFAULT_NAME;
}
