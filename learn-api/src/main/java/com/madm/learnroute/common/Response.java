package com.madm.learnroute.common;


import lombok.Data;

@Data
public class Response<T> {

    public transient static final String SUCCESS_STR = "SUCCESS";

    private T data;
    private Integer code;
    private String message;

    public Response() {
        this(200, SUCCESS_STR, null);
    }

    public Response(Integer code, String message, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public Response(T data) {
        this();
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response(data);
    }

    public static Response success() {
        return new Response(null);
    }

    public static <T> Response<T> error(int code, String msg) {
        return new Response(code, msg, null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}