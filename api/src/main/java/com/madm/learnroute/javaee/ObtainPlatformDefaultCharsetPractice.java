package com.madm.learnroute.javaee;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author dongming.ma
 * @date 2022/10/22 15:53
 */
public class ObtainPlatformDefaultCharsetPractice {
    public static void main(String[] args) {
//        //方法一
//        System.out.println(System.getProperty("file.encoding"));
//
//        //方法二
//        System.out.println(Charset.defaultCharset());
//        System.out.println(Integer.MAX_VALUE);
//        System.out.println(System.getProperty("logImpl"));
//        Object logImpl = System.getProperties().get("logImpl");
//        System.out.println(logImpl);
        //获取当前文件所在的路径
        String localPath = ObtainPlatformDefaultCharsetPractice.class.getResource("").getPath();
        System.out.println("localPath = " + localPath);


        //获取项目根目录
        String rootPath = ObtainPlatformDefaultCharsetPractice.class.getResource("/").getPath();
        System.out.println("rootPath = " + rootPath);


        URL cl = ObtainPlatformDefaultCharsetPractice.class.getClassLoader().getResource("");
        System.out.println("cl = " + cl);
        //cl = file:/C:/work/idea-WorkSpace/my-demo/demo-file/target/classes/

        String clp = cl.getPath();
        System.out.println("clp = " + clp);

        System.out.println(System.getProperty("user.dir"));

        String property = System.getProperty("sun.misc.ProxyGenerator.saveGeneratedFiles");
        System.out.println(property);
    }
}
