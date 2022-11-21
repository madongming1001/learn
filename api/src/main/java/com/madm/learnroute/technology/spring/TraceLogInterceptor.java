package com.madm.learnroute.technology.spring;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author dongming.ma
 * @date 2022/11/21 18:02
 */
public class TraceLogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> mdcMap = Maps.newConcurrentMap();
        mdcMap.put("MEETING_TRACE_LOG_ID", RandomUtil.randomString(32));
        MDC.setContextMap(mdcMap);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.clear();
    }
}
