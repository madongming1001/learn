package com.madm.learnroute.javaee;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.*;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.madm.learnroute.dto.UserDto;
import com.mdm.pojo.AuthParam;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Hutool加密第一次会加载很多加密包，导致第一次请求接口过慢，耗时长
 * 解决方案：使用google提供的加密方案
 * https://mp.weixin.qq.com/s/9Xa_ElBR3c8I29JY2k1RRg
 * <p>
 * 安全相关工具类 加密分为三种：
 * 1、对称加密（symmetric），例如：AES、DES等
 * 对称加密(也叫私钥加密)指加密和解密使用相同密钥的加密算法。
 * 2、非对称加密（asymmetric），例如：RSA、DSA等
 * 非对称加密有公钥和私钥两个概念，私钥自己拥有，不能给别人，公钥公开。根据应用的不同，我们可以选择使用不同的密钥加密：
 * 签名：使用私钥加密，公钥解密。用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。 私钥签名，公钥验签 a用私钥发给b b用公钥验证是a发送的 只有私钥和公钥是相对能打开对方的
 * 加密：用公钥加密，私钥解密。用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等
 *
 * @author dongming.ma
 * @date 2022/9/2 16:27
 */
public class EncryptPractice {

    @SneakyThrows
    public static void main(String[] args) {
        //secure();
        hmac();
        //generateSnapShotHash();
        //rsa();
    }

    private static void secure() throws NoSuchAlgorithmException {
        String source = "收拾收拾";
        String expected1 = SecureUtil.md5().digestHex(source);
        Digester digester = SecureUtil.sha1();
        //hutool提供的摘要加密算法 内部第一次初始化会加载很多第三方加密包 耗时过长
        String expected3 = MD5.create().digestHex16(source);
        String expected2 = MD5Utils.md5Hex(source.getBytes());
        String expected4 = DigestUtils.md5Hex(source);
        System.out.println(expected1.equals(expected3));
        System.out.println(expected3);
        System.out.println(expected2);
        System.out.println(expected1);
        System.out.println(expected4);
    }

    /**
     * 生成数据哈希
     */
    private static String generateSnapShotHash(UserDto userDto) {
        StringBuilder builder = new StringBuilder();
        for (AuthParam authParam : userDto.getAuth()) {
            builder.append(authParam.getAppId()).append(authParam.getAppKey());
        }
        return MD5.create().digestHex16(builder.toString());
    }

    private static void rsa() {
        RSA rsa = new RSA();
        //获得私钥
        rsa.getPrivateKey();
        rsa.getPrivateKeyBase64();
        //获得公钥
        rsa.getPublicKey();
        rsa.getPublicKeyBase64();

        //公钥加密，私钥解密，用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

        //Junit单元测试
        //Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

        //私钥加密，公钥解密，用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);

        //Junit单元测试
        Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
    }

    private static void hmac() {
        long msg = System.currentTimeMillis() / 30000;
        System.out.println(msg);
        String secret = "HV2QC3ZZL5MOHKVHAIKX5LXUUZJAC7NL";
        HMac hmac = DigestUtil.hmac(HmacAlgorithm.HmacSHA1, new SecretKeySpec(Base64.getDecoder().decode(secret), ""));
        System.out.println(new String(hmac.digest(ByteBuffer.allocate(8).putLong(msg).array())));
        System.out.println(new String(sha1(secret, msg)));
        System.out.println(new String(hmac.digest(ByteBuffer.allocate(8).putLong(msg).array())).equals(new String(sha1(secret, msg))));
    }

    private static byte[] sha1(String secret, long msg) {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret), "");//创建秘钥
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);//初始化秘钥
            byte[] value = ByteBuffer.allocate(8).putLong(msg).array();//将long类型的数据转换为byte数组
            return mac.doFinal(value);//计算数据摘要
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[20];
    }

    private static void symmetric() {
        String content = "test中文";

        //随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        //加密
        byte[] encrypt = aes.encrypt(content);
        //解密
        byte[] decrypt = aes.decrypt(encrypt);

        //加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        //解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }
}
