package com.madm.learnroute.controller;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.util.JsonFormat;
import com.madm.learnroute.annotation.UserAuthenticate;
import com.madm.learnroute.feign.UserInfoFeignClient;
import com.madm.learnroute.proto.MessageUserLogin;
import com.madm.learnroute.service.CircularServiceA;
import com.madm.learnroute.technology.messagequeue.rocketmq.message.MySource;
import com.mdm.model.Response;
import com.mdm.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/practice")
@Api(description = "练习接口", name = "PracticeController")
@RefreshScope
@Slf4j
public class PracticeController {

    @Resource
    UserInfoFeignClient userInfoFeignClient;

    @Autowired
    CircularServiceA circularServiceA;

    @Autowired
    MySource mySource;

    private ThreadLocal<MessageUserLogin.MessageUserLoginResponse.Builder> localCache = new ThreadLocal();

    @Value("${config.info:If the current value does not use the default value}")
    private String city;

    @Value("${faultToleranceTime:" + "#" + "{60 * 1000}}")
    private long faultToleranceTime;


    @PostConstruct
    public void fireAopMethod() {
        circularServiceA.methodA("aaaaaa", "bbbbbb");
    }

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
        return Response.success(userInfoFeignClient.getNacosConfig().getBody());
    }

    @PostMapping(value = "/protobufShow")
    @ApiMethod(description = "获取用户pb信息")
    public @ApiResponseObject Response getPersonProto(@RequestBody User user) {
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

    @GetMapping("/getNacosConfig")
    @ApiMethod(description = "获取nacos配置信息")
    public Response getNacosConfig() {
        return Response.success(city);
    }

    @PostMapping("/getCauseMsg")
    @ApiMethod(description = "获取错误原因信息")
    public void getCauseMsg() {
        Assert.isNull(city, "city should not be empty");

    }

    @PostMapping("/getFaultToleranceTime")
    @ApiMethod(description = "获取错误原因信息")
    public Response getFaultToleranceTime() {
        return Response.success(faultToleranceTime);
    }

    @GetMapping("/send")
    public boolean send() {
        // <2>创建 Message
        User message = new User(1);
        // <3>创建 Spring Message 对象
        Message<User> springMessage = MessageBuilder.withPayload(message).build();
        // <4>发送消息
        return mySource.erbadagangOutput().send(springMessage);
    }

    @GetMapping("/sendTrek")
    public boolean sendTrek() {
        // <2>创建 Message
        User message = new User(2);
        // <3>创建 Spring Message 对象
        Message<User> springMessage = MessageBuilder.withPayload(message).build();
        // <4>发送消息
        return mySource.trekOutput().send(springMessage);
    }

    @GetMapping("/send_delay")
    public boolean sendDelay() {
        // 创建 Message
        User message = new User(3);
        // 创建 Spring Message 对象
        Message<User> springMessage = MessageBuilder.withPayload(message).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, "3") // 设置延迟级别为 3，10 秒后消费。
                .build();
        // 发送消息
        boolean sendResult = mySource.erbadagangOutput().send(springMessage);
        log.info("[sendDelay][发送消息完成, 结果 = {}]", sendResult);
        return sendResult;
    }

    @GetMapping("/send_tag")
    public boolean sendTag() {
        for (String tag : new String[]{"trek", "specialized", "look"}) {
            // 创建 Message
            User message = new User(4);
            // 创建 Spring Message 对象
            Message<User> springMessage = MessageBuilder.withPayload(message).setHeader(MessageConst.PROPERTY_TAGS, tag) // 设置 Tag
                    .build();
            // 发送消息
            mySource.erbadagangOutput().send(springMessage);
        }
        return true;
    }

}




