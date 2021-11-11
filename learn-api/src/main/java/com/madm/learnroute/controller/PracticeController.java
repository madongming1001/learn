package com.madm.learnroute.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/practice")
public class PracticeController {

    @GetMapping(value="/practiceAccess")
    public String practiceAccess(){
        return "访问成功";
    }

}

