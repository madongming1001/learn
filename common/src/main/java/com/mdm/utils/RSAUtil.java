package com.mdm.utils;

import com.mdm.exception.RsaException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 非对称加密
 * RSA加密算法是一种非对称加密算法，所谓非对称，就是指该算法加密和解密使用不同的密钥，
 * 即使用加密密钥进行加密、解密密钥进行解密。在RAS算法中，加密密钥（即公开密钥）PK是公开信息，
 * 而解密密钥（即秘密密钥）SK是需要保密的。加密算法E和解密算法D也都是公开的。虽然解密密钥SK是由公开密钥PK决定的，
 * 由于无法计算出大数n的欧拉函数phi(N)，所以不能根据PK计算出SK。
 * 非对称算法的在应用的过程如下:
 * <p>
 * 接收方生成公钥和私钥，公钥公开，私钥保留；
 * 发送方将要发送的消息采用公钥加密，得到密文，然后将密文发送给接收方；
 * 接收方收到密文后，用自己的私钥进行解密，获得明文。
 *
 * @author dongming.ma
 * @date 2022/6/29 19:24
 */
@Slf4j
public class RSAUtil {
    public static final String PUBLIC_KEY = "public_key";

    public static final String PRIVATE_KEY = "private_key";


    public static Map<String, String> generateRasKey() {
        Map<String, String> rs = new HashMap<>();
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = null;
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥 公钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            // 将公钥和私钥保存到Map
            rs.put(PUBLIC_KEY, publicKeyString);
            rs.put(PRIVATE_KEY, privateKeyString);
        } catch (Exception e) {
            log.error("RsaUtils invoke genKeyPair failed.", e);
            throw new RsaException("RsaUtils invoke genKeyPair failed.");
        }
        return rs;
    }


    public static String encrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("RsaUtils invoke encrypt failed.", e);
            throw new RsaException("RsaUtils invoke encrypt failed.");
        }
    }


    public static String decrypt(String str, String privateKey) {

        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            log.error("RsaUtils invoke decrypt failed.", e);
            throw new RsaException("RsaUtils invoke decrypt failed.");
        }

    }
}
