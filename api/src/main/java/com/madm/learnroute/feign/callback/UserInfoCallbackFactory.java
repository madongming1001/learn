package com.madm.learnroute.feign.callback;

import com.madm.learnroute.common.Response;
import com.madm.learnroute.feign.UserInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author dongming.ma
 * @date 2022/6/1 19:50
 */
@Slf4j
@Component
public class UserInfoCallbackFactory implements FallbackFactory<UserInfoFeignClient> {
    @Override
    public UserInfoFeignClient create(Throwable cause) {
        return () -> {
            log.error(cause.getMessage());
            return Response.error("请求用户信息服务失败！");
        };
    }
}
