package com.madm.learnroute.service;

/**
 * @author dongming.ma
 * @date 2023/3/18 17:00
 */

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.madm.learnroute.config.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author cxt
 * @date 2021/11/20
 */
@Service
@Slf4j
public class PayService {

    /**
     * 支付宝支付
     */
    public void alipay(String registerId, boolean isAppoint, HttpServletResponse response) throws IOException {
        DefaultAlipayClient client = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        // todo 查询金额相关信息
        // 付款金额，必填
        String total_amount = String.valueOf(0.1);

        // 订单名称，必填
        String subject = "测试的名称";

        // 商品描述，可空
        String body = "就诊日期：" + new Date();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", registerId);
        bizContent.put("total_amount", total_amount);
        bizContent.put("subject", subject);
        bizContent.put("body", body);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        alipayRequest.setBizContent(bizContent.toString());

        // 生成请求页面
        String form = "";
        try {
            form = client.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 支付校验
     *
     * @param registerId 订单号
     * @return
     */
    public boolean alipayCheck(String registerId) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", registerId);
        request.setBizContent(bizContent.toString());

        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {

                // TODO 异步通知接收不到，先用同步更新状态，上线时删除 --- 开始
                if (response.getTradeNo() != null) {
                    // update 订单表
                }
                // TODO 异步通知接收不到，先用同步更新状态，上线时删除 --- 结束


                return true;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 异步付款结果通知
     * 注意：支付宝服务器发送的url是没有登录状态的，需要在系统中设置放过登录校验，不然接收不到通知
     */
    public void alipayCallback(HttpServletRequest request) throws Exception {
        String tradeStatus = request.getParameter("trade_status"); // 交易状态
        String orderNo = request.getParameter("out_trade_no"); // 获取订单号
        String tradeNo = request.getParameter("trade_no"); // 支付宝交易凭证号

        Map<String, String> map = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if (requestParams.isEmpty()) {
            throw new Exception("系统异常，异步通知参数为空");
        }
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }

        // 校验
        boolean signVerified = AlipaySignature.rsaCheckV1(map, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.sign_type);

        if (signVerified) {
            if (tradeStatus.equals("TRADE_SUCCESS")) {
                // 支付成功 -> 自己实现改订单状态
                // update 订单表   orderNo, tradeNo
            } else {
                // 其他可能触发的异步通知
                log.debug(tradeStatus);
            }
        }
    }

    /**
     * 支付宝退费
     *
     * @param outTradeNo   订单号
     * @param tradeNo      交易支付凭证号
     * @param refundAmount 交易金额
     * @param refundReason 退费原因
     */
    public boolean alipayRefund(String outTradeNo, String tradeNo, String refundAmount, String refundReason) throws AlipayApiException {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("trade_no", tradeNo);
        bizContent.put("refund_amount", refundAmount);
        bizContent.put("refund_reason", refundReason);

        alipayRequest.setBizContent(bizContent.toString());

        boolean result = false;
        AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);
        boolean b = response.isSuccess();
        if (!b) {
            // 退款失败，重试三次
            for (int i = 0; i < 3; i++) {
                AlipayTradeRefundResponse loopResponse = alipayClient.execute(alipayRequest);
                boolean loopResult = loopResponse.isSuccess();
                if (loopResult) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                log.error("订单：{}，退款失败", outTradeNo);
            }
        } else {
            log.info("订单：{}，退款成功", outTradeNo);
            result = true;
        }
        return result;
    }


    /**
     * 支付宝退款查询
     *
     * @param outRequestNo 订单号
     * @param tradeNo      交易支付凭证号
     */
    public boolean alipayRefundSelect(String outRequestNo, String tradeNo) {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", tradeNo);
        bizContent.put("out_request_no", outRequestNo);
        alipayRequest.setBizContent(bizContent.toString());

        boolean result = false;
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(alipayRequest);
            if (!response.isSuccess()) {
                for (int i = 0; i < 3; i++) {
                    AlipayTradeFastpayRefundQueryResponse loopResponse = alipayClient.execute(alipayRequest);
                    if (loopResponse.isSuccess()) {
                        result = true;
                        break;
                    }
                }
            } else {
                log.info("已退款");
                result = true;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

}

