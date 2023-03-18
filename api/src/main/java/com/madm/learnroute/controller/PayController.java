package com.madm.learnroute.controller;

/**
 * @author dongming.ma
 * @date 2023/3/18 16:59
 */

import com.alipay.api.AlipayApiException;
import com.madm.learnroute.service.PayServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: cxt
 * @time: 2021/11/18
 */
@RestController
@Api(tags = "支付接口")
@RequestMapping("/ih/diagnose/pay")
public class PayController {

    private PayServiceImpl payServiceImpl;

    // "支付宝支付接口"
    @PostMapping("/alipay")
    public void alipay(@RequestParam String registerId, @RequestParam boolean isAppoint, HttpServletResponse response) {
        try {
            payServiceImpl.alipay(registerId, isAppoint, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO 可以不写这个接口，个人认为作用不大
    // "支付验证"
    @PostMapping("/alipay/check")
    public boolean alipayCheck(@RequestParam String registerId) {
        return payServiceImpl.alipayCheck(registerId);
    }

    /**
     * 支付宝异步付款结果通知
     */
    @PostMapping("/alipay/callback")
    public void alipayCallback(HttpServletRequest request) {
        try {
            payServiceImpl.alipayCallback(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝退款
     * 注意：支付宝服务器发送的url是没有登录状态的，需要在系统中设置放过登录校验，不然接收不到通知
     */
    @PostMapping("/alipay/refund/callback")
    public boolean alipayRefundCallback(@RequestParam String outTradeNo, @RequestParam String tradeNo, @RequestParam String refundAmount, @RequestParam String subject) {
        boolean result = false;
        try {
            result = payServiceImpl.alipayRefund(outTradeNo, tradeNo, refundAmount, subject);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 退费查询
    @PostMapping("/alipay/refund/alipayRefundSelect")
    public boolean alipayRefundCallback(@RequestParam String outRequestNo, @RequestParam String tradeNo) {
        return payServiceImpl.alipayRefundSelect(outRequestNo, tradeNo);
    }

    @Autowired
    public void setPayService(PayServiceImpl payServiceImpl) {
        this.payServiceImpl = payServiceImpl;
    }
}

