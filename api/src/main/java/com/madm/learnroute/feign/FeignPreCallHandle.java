package com.madm.learnroute.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign调用之前回掉
 */
public class FeignPreCallHandle implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate rs) {
        String method = rs.method();
    }

}
