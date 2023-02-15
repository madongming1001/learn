package com.madm.learnroute.technology.spring.interceptor;

import com.madm.learnroute.constant.TraceLogEnum;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author dongming.ma
 * @date 2023/2/19 15:32
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {

    //restTemplate.setInterceptors(Arrays.asList(new RestTemplateTraceIdInterceptor()));

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String traceId = MDC.get(TraceLogEnum.TRACE_LOG_ID.getValue());
        if (traceId != null) {
            request.getHeaders().add(TraceLogEnum.TRACE_LOG_ID.getValue(), traceId);
        }

        return execution.execute(request, body);
    }
}
