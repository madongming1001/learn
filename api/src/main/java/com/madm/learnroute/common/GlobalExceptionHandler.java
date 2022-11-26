package com.madm.learnroute.common;

import cn.hutool.http.HttpStatus;
import com.mdm.model.RestResponse;
import com.mdm.utils.ExceptionUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    public RestResponse<String> handleIllegalArgumentException(Exception e) {
        return RestResponse.code(HttpStatus.HTTP_INTERNAL_ERROR).withMsg(ExceptionUtil.getCauseMsg(e)).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public RestResponse<String> handleRuntimeException(Exception e) {
        return RestResponse.code(HttpStatus.HTTP_INTERNAL_ERROR).withMsg(ExceptionUtil.getCauseMsg(e)).build();
    }

    //    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        });
    }
}
