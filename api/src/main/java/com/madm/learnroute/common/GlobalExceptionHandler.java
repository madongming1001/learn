package com.madm.learnroute.common;

import com.mdm.model.RestResponse;
import com.mdm.util.ExceptionUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * @author dongming.ma
 * @date 2022/6/6 21:47
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * For IllegalArgumentException, we are returning void with status code as 400, so our error-page will be used in
     * this case.
     *
     * @throws IllegalArgumentException IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public RestResponse<String> handleIllegalArgumentException(Exception ex) throws IOException {
        return RestResponse.code(500).withMsg(ExceptionUtil.getCauseMsg(ex)).build();
    }
}
