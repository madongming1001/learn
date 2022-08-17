package com.madm.learnroute.javaee;

import java.nio.charset.Charset;

/**
 * @author dongming.ma
 * @date 2022/10/22 15:53
 */
public class ObtainPlatformDefaultCharsetPractice {
    public static void main(String[] args) {
        //方法一
        System.out.println(System.getProperty("file.encoding"));

        //方法二
        System.out.println(Charset.defaultCharset());
        System.out.println(Integer.MAX_VALUE);
    }
}
