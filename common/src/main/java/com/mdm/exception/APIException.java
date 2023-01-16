package com.mdm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author dongming.ma
 * @date 2023/1/16 20:40
 */
@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    // 手动设置异常
    public APIException(HttpStatus status, String message) {
        super(message);
        this.code = status.value();
        this.msg = status.getReasonPhrase();
    }

    // 默认异常使用APP_ERROR状态码
    public APIException(String message) {
        super(message);
        this.code = INTERNAL_SERVER_ERROR.value();
        this.msg = INTERNAL_SERVER_ERROR.getReasonPhrase();
    }
}