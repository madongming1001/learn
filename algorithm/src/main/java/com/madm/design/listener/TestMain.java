package com.madm.design.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 6:55 PM
 */
@Slf4j
public class TestMain {

    public static void main(String[] args) {
        LotteryService lotteryService = new LotteryServiceImpl();
        LotteryResult result = lotteryService.draw("2765789109876");
        log.info("测试结果：{}", JSON.toJSONString(result));
    }
}
