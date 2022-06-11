package com.madm.learnroute.technology.spring;

import com.madm.learnroute.annotation.Whitelist;
import com.mdm.pojo.AuthParam;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:53 PM
 */
//@Component
public class AuthParamResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(AuthParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, Message<?> message) throws Exception {
        Whitelist whitelist = methodParameter.getMethodAnnotation(Whitelist.class);
        // 通过 webRequest 和 whitelist 校验白名单
        return new AuthParam();
    }
}
