package com.madm.learnroute.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.util.JsonFormat;
import com.madm.learnroute.annotation.UserAuthenticate;
import com.madm.learnroute.common.Response;
import com.madm.learnroute.pojo.User;
import com.madm.learnroute.proto.MessageUserLogin;
import com.madm.learnroute.service.PracticeService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/practice")
@Api(description = "练习接口", name = "PracticeController")
public class PracticeController {

    private ThreadLocal<MessageUserLogin.MessageUserLoginResponse.Builder> localCache = new ThreadLocal();

    @UserAuthenticate
    @RequestMapping("/success")
    @ApiMethod(description = "成功响应")
    public Response restSuccess() {
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
        });
        return Response.success();
    }

    @PostMapping(value = "/protobufShow")
    @ApiMethod(description = "获取用户pb信息")
    public @ApiResponseObject
    Response getPersonProto(@RequestBody User user) {
        try {
            MessageUserLogin.MessageUserLoginResponse.Builder builder = MessageUserLogin.MessageUserLoginResponse.newBuilder();
            String token = UUID.randomUUID().toString();
            builder.setAccessToken(token);
            builder.setUsername(user.getName());

            localCache.set(builder);
            MessageUserLogin.MessageUserLoginResponse.Builder localCaches = localCache.get();
            localCaches.setAccessToken(UUID.randomUUID().toString());
            if (localCaches.getAccessToken().equals(token)) {
                return new Response("ThreadLocal存储pb值get之后修改之前设置的pb值也会修改，无需重新set。");
            }
            JSON json = (JSON) JSONObject.toJSON(JsonFormat.printer().includingDefaultValueFields().print(builder));
            return new Response(JSONObject.toJavaObject(json, User.class));
        } catch (Exception e) {
            return Response.exception(e);
        }

    }

}


