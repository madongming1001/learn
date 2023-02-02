package com.madm.learnroute.technology.spring;

import cn.hutool.core.util.RandomUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.madm.learnroute.constant.TraceLogEnum.TRACE_LOG_ID;


/**
 * @author dongming.ma
 * @date 2022/11/21 18:02
 */
public class TraceLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果有上层调用就用上层的ID
        String traceId = request.getHeader(TRACE_LOG_ID.getValue());
        if (traceId == null) {
            traceId = getTraceId();
        }
        MDC.put(TRACE_LOG_ID.getValue(), traceId);
        ThreadLocalHolder.initScene(traceId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(TRACE_LOG_ID.getValue());
    }

    public static String getTraceId() {
        return RandomUtil.randomString(32);
    }

}
