package com.mdm.exception;

/**
 * @author dongming.ma
 * @date 2022/6/29 19:39
 */
public class RsaException extends RuntimeException {

    public RsaException(String message) {
        super(message);
    }

    public RsaException() {
        super(null, null);
    }

}