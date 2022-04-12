package com.madm.learnroute.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign调用之前回掉
 */
public class FeignPreCallProcessing implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

    }
}
