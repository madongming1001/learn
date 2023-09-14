package com.madm.learnroute.javaee;

import com.mdm.utils.PrintUtil;
import lombok.SneakyThrows;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.Security;

/**
 * @author dongming.ma
 * @date 2023/9/14 01:55
 */

class Hash {
    private MessageDigest digest;

    @SneakyThrows
    public Hash(String algorithm) {
        this.digest = MessageDigest.getInstance(algorithm);
    }

    public String hashCode(String str) {
        return DatatypeConverter.printHexBinary(digest.digest(str.getBytes())).toUpperCase();
    }
}
public class SecurityPractice {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("支持的算法：");
        for (String messageDigest : Security.getAlgorithms("MessageDigest")) {
            System.out.println(messageDigest);
        }
        PrintUtil.printSplitLine();
        /**
         * 	MD2("MD2"),
         * 	MD5("MD5"),
         * 	SHA1("SHA-1"),
         * 	SHA256("SHA-256"),
         * 	SHA384("SHA-384"),
         * 	SHA512("SHA-512");
         */
        String algorithm = "SHA";
        Hash hash = new Hash(algorithm);
        String input1 = "zuochengyunzuochengyun1";
        String input2 = "zuochengyunzuochengyun2";
        String input3 = "zuochengyunzuochengyun3";
        String input4 = "zuochengyunzuochengyun4";
        String input5 = "zuochengyunzuochengyun5";
        System.out.println(hash.hashCode(input1));
        System.out.println(hash.hashCode(input2));
        System.out.println(hash.hashCode(input3));
        System.out.println(hash.hashCode(input4));
        System.out.println(hash.hashCode(input5));
    }
}
