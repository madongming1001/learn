package com.madm.learnroute.controller;

import com.madm.learnroute.auth.UserAuthenticate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/practice")
/**
 *
 */
public class PracticeController {

    @UserAuthenticate
    @GetMapping(value="/practiceAccess")
    public String practiceAccess(){
        System.out.println("走到了practiceAccess方法");
        return "访问成功";
    }

}

