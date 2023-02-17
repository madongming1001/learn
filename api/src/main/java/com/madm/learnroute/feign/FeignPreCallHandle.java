package com.madm.learnroute.feign;

import com.madm.learnroute.technology.spring.ThreadLocalHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign调用之前回掉
 */
@Slf4j
@Component
public class FeignPreCallHandle implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate rs) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURI = request.getRequestURI();
        String dispatcherURI = rs.url();
        String traceId = ThreadLocalHolder.getScene();
        log.info("set traceId: requestURI[{}], dispatcherURI[{}], traceId[{}]", requestURI, dispatcherURI, traceId);
        rs.path();
    }

}
