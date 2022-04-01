package com.madm.learnroute.javaee;


import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 9:56 AM
 */
@Slf4j
public class PatternPractice {
    public static void main(String[] args) {
        String regex = "^(([1-9]-UA-)|(TA-))([0-9]{17,19})";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher("1-UA-12345678910000000000");
//        Matcher matcher = compile.matcher("TA-1509801876382683111");
        System.out.println(matcher.find());
    }
}

