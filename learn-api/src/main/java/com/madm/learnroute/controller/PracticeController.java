package com.madm.learnroute.controller;

import com.madm.learnroute.common.Response;
import com.madm.learnroute.pojo.AuthParam;
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
    public Response getPersonProto(@RequestBody MessageUserLogin.MessageUserLoginRequest request) {
        MessageUserLogin.MessageUserLoginResponse.Builder builder = MessageUserLogin.MessageUserLoginResponse.newBuilder();
        builder.setAccessToken(UUID.randomUUID().toString());
        builder.setUsername(request.getUsername());
        return new Response(builder.build().toByteString());
    }

}

