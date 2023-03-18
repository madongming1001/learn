package com.madm.learnroute.service.impl;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dongming.ma
 * @date 2023/3/18 17:04
 */
public interface PayService {
    void alipay(String registerId, boolean isAppoint, HttpServletResponse response) throws IOException;

    boolean alipayCheck(String registerId);

    void alipayCallback(HttpServletRequest request) throws Exception;

    boolean alipayRefund(String outTradeNo, String tradeNo, String refundAmount, String refundReason) throws AlipayApiException;

    boolean alipayRefundSelect(String outRequestNo, String tradeNo);
}
