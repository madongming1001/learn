package com.madm.learnroute.feign;

import com.mdm.model.Response;
import com.madm.learnroute.feign.callback.UserInfoCallbackFactory;
import com.mdm.pojo.User;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RefreshScope
@FeignClient(name = "learn-api", url = "${feign.server.nacosconfig}", fallbackFactory = UserInfoCallbackFactory.class)
@Component
public interface UserInfoFeignClient {

    @GetMapping("practice/getNacosConfig")
    Response getNacosConfig();

    @PostMapping("practice/protobufShow")
    Response getPersonProto(@RequestBody User user);

    default void print() {
        System.out.println("sssss");
    }
}
