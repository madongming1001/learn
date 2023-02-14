package com.madm.learnroute.technology.spring.interceptor;

import com.madm.learnroute.constant.TraceLogEnum;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author dongming.ma
 * @date 2023/2/19 15:27
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {

    private static HttpClientBuilder httpClient;

    static {
        httpClient = HttpClientBuilder.create().addInterceptorFirst(new HttpClientTraceIdInterceptor());
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        String traceId = MDC.get(TraceLogEnum.TRACE_LOG_ID.getValue());
        //当前线程调用中有traceId，则将该traceId进行透传
        if (traceId != null) {
            //添加请求体
            request.addHeader(TraceLogEnum.TRACE_LOG_ID.getValue(), traceId);
        }
    }
}
