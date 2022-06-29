package com.mdm.exception;

/**
 * @author dongming.ma
 * @date 2022/6/29 19:39
 */
public class RsaException extends RuntimeException {

    private final String message;

    public RsaException(String message) {
        this.message = message;
    }

}