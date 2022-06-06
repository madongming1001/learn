package com.madm.learnroute.feign.callback;

import com.madm.learnroute.common.Response;
import com.madm.learnroute.feign.UserInfoFeignClient;
import com.mdm.pojo.User;
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
        return new UserInfoFeignClient() {
            @Override
            public Response getNacosConfig() {
                log.error(cause.getMessage());
                return Response.error("请求getNacosConfig()失败！");
            }

            @Override
            public Response getPersonProto(User user) {
                log.error(cause.getMessage());
                return Response.error("请求getPersonProto()失败！");
            }
        };
    }
}
