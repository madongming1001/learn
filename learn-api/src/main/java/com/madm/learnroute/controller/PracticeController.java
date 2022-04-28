package com.madm.learnroute.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.madm.learnroute.common.Response;
import com.madm.learnroute.pojo.AuthParam;
import com.madm.learnroute.pojo.User;
import com.madm.learnroute.proto.MessageUserLogin;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/practice")
@Api(description = "练习接口", name = "PracticeController")
public class PracticeController {

    private ThreadLocal<MessageUserLogin.MessageUserLoginResponse.Builder> localCache = new ThreadLocal();

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

    @RequestMapping(value = "/protobufShow")
    @ApiMethod(description = "获取用户pb信息")
    public Response getPersonProto(@RequestBody User user){
        MessageUserLogin.MessageUserLoginResponse.Builder builder = MessageUserLogin.MessageUserLoginResponse.newBuilder();
        String token = UUID.randomUUID().toString();
        builder.setAccessToken(token);
        builder.setUsername(user.getName());
        localCache.set(builder);
        MessageUserLogin.MessageUserLoginResponse.Builder localCaches = localCache.get();
        localCaches.setAccessToken(UUID.randomUUID().toString());
        if (localCaches.getAccessToken().equals(token)) {
            new Response("ThreadLocal存储pb值get之后修改之前设置的pb值也会修改，无需重新set。");
        }
        try {
            return new Response(JsonFormat.printer().includingDefaultValueFields().print(builder));
        } catch (InvalidProtocolBufferException e) {
        }
        return Response.error(1,"builder未知字段");
    }

}


