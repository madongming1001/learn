package com.mdm.model;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response<T> {

    public transient static final String SUCCESS_STR = "SUCCESS";

    private T body;
    private Integer code;
    private Object message;

    public Response() {
        this(200, SUCCESS_STR, null);
    }

    public Response(Integer code, Object message, T body) {
        this.message = message;
        this.code = code;
        this.body = body;
    }

    public Response(T body) {
        this();
        this.body = body;
    }

    public static <T> Response<T> success(T body) {
        return new Response(body);
    }

    public static Response success() {
        return new Response(null);
    }

    public static <T> Response<T> error(int code, Object msg) {
        return new Response(code, msg, null);
    }

    public static <T> Response<T> error(Object msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(),msg);
    }

    public static <T> Response<T> exception(Object msg) {
        return new Response(505, msg.toString(), null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(message).append('\'');
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}