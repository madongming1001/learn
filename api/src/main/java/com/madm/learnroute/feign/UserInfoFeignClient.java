package com.madm.learnroute.feign;

import com.madm.learnroute.common.Response;
import com.madm.learnroute.feign.callback.UserInfoCallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "learn-api",url = "${feign.server.nacosconfig}",fallbackFactory = UserInfoCallbackFactory.class)
@Component
public interface UserInfoFeignClient {

    @PostMapping("practice/getNacosConfig")
    Response getNacosConfig();
}
