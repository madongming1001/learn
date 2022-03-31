package com.madm.learnroute.controller;

import com.alibaba.fastjson.JSON;
import com.madm.learnroute.annotation.Whitelist;
import com.madm.learnroute.annotation.UserAuthenticate;
import com.madm.learnroute.pojo.AuthParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/practice")
public class PracticeController {

    @UserAuthenticate
    @GetMapping(value = "/practiceAccess")
    @Whitelist
    public String practiceAccess(@RequestBody @Validated AuthParam authParam) {
        return JSON.toJSONString(authParam);
    }

}

