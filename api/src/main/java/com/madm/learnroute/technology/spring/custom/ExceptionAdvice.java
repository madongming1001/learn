package com.madm.learnroute.technology.spring.custom;

import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;

//@RestControllerAdvice
public class ExceptionAdvice {
    // 将Spring DataBinder 配置为使用直接字段访问
    @InitBinder
    private void activateDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

}
