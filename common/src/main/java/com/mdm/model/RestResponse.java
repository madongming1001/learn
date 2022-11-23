package com.mdm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * @author dongming.ma
 * @date 2022/5/28 18:23
 */
@Accessors(chain = true)
@Data
public class RestResponse<T>{
    private int code;

    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T body;

    public RestResponse() {
    }

    public RestResponse(int code, String message, T body) {
        this.code = code;
        this.setMessage(message);
        this.body = body;
    }

    public RestResponse(int code, T body) {
        this.code = code;
        this.body = body;
    }

    public RestResponse(int code, String message) {
        this.code = code;
        this.setMessage(message);
    }

    public static RestResponse OK() {
        return new RestResponse(200, "success");
    }

    public boolean ok() {
        return this.code == 0 || this.code == 200;
    }

    public static ResResultBuilder code(int code) {
        return new ResResultBuilder(code);
    }

    @Override
    public String toString() {
        return "RestResponse{" + "code=" + code + ", message='" + message + '\'' + ", body=" + body + '}';
    }

    public static <T> ResResultBuilder<T> builder() {
        return new ResResultBuilder<T>();
    }

    public static final class ResResultBuilder<T> {

        private int code;

        private String errMsg;

        private T body;

        private ResResultBuilder() {
        }

        public ResResultBuilder(int code) {
            this.code = code;
        }

        public ResResultBuilder<T> withCode(int code) {
            this.code = code;
            return this;
        }

        public ResResultBuilder<T> withMsg(String errMsg) {
            this.errMsg = errMsg;
            return this;
        }

        public ResResultBuilder<T> withBody(T body) {
            this.body = body;
            return this;
        }

        /**
         * Build result.
         *
         * @return result
         */
        public RestResponse<T> build() {
            RestResponse<T> RestResponse = new RestResponse<T>();
            RestResponse.setCode(code);
            RestResponse.setMessage(errMsg);
            RestResponse.setBody(body);
            return RestResponse;
        }
    }
}