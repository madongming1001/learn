package com.madm.learnroute.javaee;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author dongming.ma
 * @date 2022/9/2 16:27
 */
public class EncryptPractice {

    public static void main(String[] args) {
        String encrypt = DigestUtils.md5Hex("收拾收拾");
        System.out.println(encrypt);
    }
}
