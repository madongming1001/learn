package com.mdm.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author dongming.ma
 * @date 2022/5/28 18:23
 */
@Accessors(chain = true)
@Data
public class RestResponse<T> {
    private int code;

    private String msg;

    //    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T body;

    public RestResponse() {
    }

    public RestResponse(int code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public RestResponse(int code, T body) {
        this.code = code;
        this.body = body;
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RestResponse(Object body) {
        this.body = (T) body;
    }

    public static RestResponse OK() {
        return new RestResponse(200, "success");
    }

    public static ResResultBuilder code(int code) {
        return new ResResultBuilder(code);
    }

    public static <T> ResResultBuilder<T> builder() {
        return new ResResultBuilder<T>();
    }

    public boolean ok() {
        return this.code == 0 || this.code == 200;
    }

    @Override
    public String toString() {
        return "RestResponse{" + "code=" + code + ", message='" + msg + '\'' + ", body=" + body + '}';
    }

    public static final class ResResultBuilder<T> {

        private int code;

        private String msg;

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

        public ResResultBuilder<T> withMsg(String msg) {
            this.msg = msg;
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
            RestResponse.setCode(this.code);
            RestResponse.setMsg(this.msg);
            RestResponse.setBody(this.body);
            return RestResponse;
        }
    }
}