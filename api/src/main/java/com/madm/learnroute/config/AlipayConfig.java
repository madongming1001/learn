package com.madm.learnroute.config;

/**
 * @author dongming.ma
 * @date 2023/3/18 16:59
 */
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2021******2179"; // 写你的AppID

    // 应用，您的PKCS8格式RSA2私钥
    public static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCurNMo4AdLzKibsfGiz469NmXaGgypz2LBcKUwyXG4Cn0UAGDnxX+7+sshGyxMYrEczETZ8rRR6d7M4ZzyFGeHZN4aHCsUT7MyIVu+OtUpZr+H4zVllKbia7rCZgkWd8/r+kpxY1ikFhxWpu+hPi32ylA+9FdKPXSJrOq************************"; // 写你商户私钥

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiWEgdjid1gy2nqKcNcoJTr7f9om6WNI6qaNnv0KW3vjjBHnrgnZIGHuI3XFpPyii0rtkVpRTEZcD1JBq21rPfPmS3EW7AVqdjPWTuz/7EgH2OvR4Sk/pvS9Lg7IqnJwG29r43WYwQ22x+K+CZSzTKmf5Vzk8m2cKumogvTyNf26+c/SIYVn4************************"; // 写你的支付宝公钥

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(开发环境时可以使用内网穿透工具，有很多，自行度)
    public static String notify_url = "http://pcfapi.natappfree.cc/ih/diagnose/pay/alipay/callback"; // 写你接受通知的接口地址

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(其实就是支付成功后返回的页面)
    public static String return_url = "http://172.16.16.102:8080/#/seeDoctor"; // 写你在支付成功之后进入的页面地址

    // 支付宝网关
    public static String log_path = "C:\\";
}
