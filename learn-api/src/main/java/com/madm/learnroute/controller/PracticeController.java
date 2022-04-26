package com.madm.learnroute.controller;

import com.madm.learnroute.common.Response;
import com.madm.learnroute.pojo.AuthParam;
import com.madm.learnroute.pojo.User;
import com.madm.learnroute.proto.MessageUserLogin;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/practice")
public class PracticeController {

    private ThreadLocal<MessageUserLogin.MessageUserLoginResponse.Builder> localCache = new ThreadLocal();

    //    @UserAuthenticate(permission = true)
    @GetMapping(value = "/practiceAccess")
//    @Whitelist
    public Response practiceAccess(@RequestBody @Valid @Validated AuthParam authParam) {
        return Response.success(authParam);
    }

    @RequestMapping("/success")
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
    public Response getPersonProto(@RequestBody User user) {
        MessageUserLogin.MessageUserLoginResponse.Builder builder = MessageUserLogin.MessageUserLoginResponse.newBuilder();
        builder.setAccessToken(UUID.randomUUID().toString());
        builder.setUsername(user.getName());
        localCache.set(builder);
        MessageUserLogin.MessageUserLoginResponse.Builder localCaches = localCache.get();
        localCaches.setUsername("修改之后的userName");
        localCaches.setAccessToken("修改之后的access_token");
        System.out.println(builder.toString());
        return new Response(builder.build().toByteString());
    }

}

