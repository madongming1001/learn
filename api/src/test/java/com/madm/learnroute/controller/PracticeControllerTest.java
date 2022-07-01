package com.madm.learnroute.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.madm.learnroute.proto.MessageUserLogin;
import com.mdm.model.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class PracticeControllerTest {

    @Test
    void getPersonProto() {
        MessageUserLogin.MessageUserLoginRequest.Builder builder = MessageUserLogin.MessageUserLoginRequest.newBuilder();
        builder.setUsername("tom");
        builder.setPassword("123456");
        HttpRequest request = HttpUtil.createPost("http://localhost:8080/practice/protobufShow");
        request.body(builder.build().toByteArray());
        request.header("Content-Type", "application/x-protobuf");
        HttpResponse response = request.execute();
        System.out.println(JSONObject.parseObject(response.body(), Response.class));
    }
}