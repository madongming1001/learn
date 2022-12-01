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
        String content = "目前，《网络安全法》《数据安全法》《个人信息保护法》均从法律层面对数据的分类分级保护提出要求。各企业也陆续成立了由法务部、数据部、安全部、隐私管理部以及相关业务部门组成的联合工作组开展企业数据分类分级工作，以优化企业数据分类分级标准，应对监管，确保数据合规。\n" +
                "《网络安全法》第二十一条提出，国家实施网络安全等级保护制度，针对数据采取数据分类、重要数据备份和加密等措施。";
        System.out.println(content.matches(".*(目前|个人信息).*"));
//        String regex = "^([1-9]-(UA|TA)-)([0-9]{17,})";
        String regex = "(目前|个人信息)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
//        Matcher matcher = compile.matcher("TA-1509801876382683111");
        System.out.println(matcher.find());

    }
}

