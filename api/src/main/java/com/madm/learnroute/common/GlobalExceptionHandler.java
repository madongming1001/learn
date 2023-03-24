package com.madm.learnroute.common;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdm.exception.APIException;
import com.mdm.model.RestResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author dongming.ma
 * @date 2022/6/6 21:47
 */
//@RestControllerAdvice
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {
    /**
     * For IllegalArgumentException, we are returning void with status code as 400, so our error-page will be used in
     * this case.
     *
     * @throws IllegalArgumentException IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public RestResponse handleIllegalArgumentException(Exception e) {
        return RestResponse.code(INTERNAL_SERVER_ERROR.value()).withBody(ExceptionUtil.getMessage(e)).withMsg(INTERNAL_SERVER_ERROR.getReasonPhrase()).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public RestResponse handleRuntimeException(Exception e) {
        return RestResponse.code(INTERNAL_SERVER_ERROR.value()).withBody(e).withMsg(INTERNAL_SERVER_ERROR.getReasonPhrase()).build();
    }

    @ExceptionHandler(APIException.class)
    public RestResponse handleAPIException(APIException e) {
        return RestResponse.code(e.getCode()).withBody(ExceptionUtil.getMessage(e)).withMsg(e.getMsg()).build();
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // response是 RestResponse 类型不进行包装
        return !methodParameter.getParameterType().isAssignableFrom(RestResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResultVo里后转换为json串进行返回
                return objectMapper.writeValueAsString(new RestResponse<>(data));
            } catch (JsonProcessingException e) {
                throw new APIException(INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        // 否则直接包装成ResultVo返回
        return new RestResponse<>(data);
    }
}
