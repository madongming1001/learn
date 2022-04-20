package com.madm.learnroute.controller;

import com.madm.learnroute.annotation.UserAuthenticate;
import com.madm.learnroute.annotation.Whitelist;
import com.madm.learnroute.common.Response;
import com.madm.learnroute.pojo.AuthParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/practice")
public class PracticeController {

//    @UserAuthenticate(permission = true)
    @GetMapping(value = "/practiceAccess")
//    @Whitelist
    public Response practiceAccess(@RequestBody @Valid @Validated AuthParam authParam) {
        return Response.success(authParam);
    }

    @GetMapping("/getSuccess")
    public Response restSuccess() {
        return Response.success();
    }

}

