package com.madm.learnroute.technology.spring.interceptor;

import cn.hutool.core.util.StrUtil;
import com.madm.learnroute.constant.TraceLogEnum;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author dongming.ma
 * @date 2023/2/19 15:30
 */
public class OkHttpTraceIdInterceptor implements Interceptor {

    private static OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new OkHttpTraceIdInterceptor()).build();

    @Override
    public Response intercept(Chain chain) throws IOException {
        String traceId = MDC.get(TraceLogEnum.TRACE_LOG_ID.getValue());
        Request request = null;
        if (StrUtil.isNotEmpty(traceId)) {
            //添加请求体
            request = chain.request().newBuilder().addHeader(TraceLogEnum.TRACE_LOG_ID.getValue(), traceId).build();
        }
        Response originResponse = chain.proceed(request);
        return originResponse;
    }
}

