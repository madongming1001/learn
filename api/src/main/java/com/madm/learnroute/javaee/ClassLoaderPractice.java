package com.madm.learnroute.javaee;

import cn.hutool.core.io.resource.UrlResource;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import static com.mdm.utils.PrintUtil.printSplitLine;


/**
 * bootstrapLoader加载以下文件：-Xbootclasspath、sun.boot.class.path
 * extClassloader加载以下文件：java.ext.dirs
 * appClassLoader加载以下文件：java.class.path
 */
public class ClassLoaderPractice {
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";

    ClassLoaderPractice() {
        System.out.println(this.getClass().getName());
    }

    @SneakyThrows
    public static void main(String[] args) {
        ClassLoaderPractice classLoaderPractice = new ClassLoaderPractice();
        System.out.println(classLoaderPractice);
        System.out.println(String.class.getClassLoader());
        System.out.println(ClassLoaderPractice.class.getName());
//        System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader().getClass().getName());
        System.out.println(ClassLoaderPractice.class.getClassLoader().getClass().getName());

        System.out.println();
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassloader = appClassLoader.getParent();
        ClassLoader bootstrapLoader = extClassloader.getParent();
        System.out.println("the bootstrapLoader : " + bootstrapLoader);
        System.out.println("the extClassloader : " + extClassloader);
        System.out.println("the appClassLoader : " + appClassLoader);

        printSplitLine();
        System.out.println("bootstrapLoader加载以下文件：");//-Xbootclasspath
        System.out.println(System.getProperty("sun.boot.class.path"));

        System.out.println();
        System.out.println("extClassloader加载以下文件：");
        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println();
        System.out.println("appClassLoader加载以下文件：");
        System.out.println(System.getProperty("java.class.path"));

        // 出发都是舍弃小数位
        System.out.println(String.class.getClassLoader());
        System.out.println(ClassLoaderPractice.class.getClassLoader());
        System.out.println(7 / 2);
        System.out.println(7 >>> 1);
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/com/madm/learnroute/javaee/ 当前同级目录下去找
        System.out.println(ClassLoaderPractice.class.getResource("META-INF/spring.factories"));/**/
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/ 上层目录下去找
        System.out.println(ClassLoaderPractice.class.getResource("/logback-spring.xml"));
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/ 上层目录下去找
        System.out.println(ClassLoaderPractice.class.getClassLoader().getResource("META-INF/spring.factories"));
        //null 同级目录下去找
        MultiValueMap<String, String> result = result = new LinkedMultiValueMap<>();
        ClassLoader classLoader = ClassLoaderPractice.class.getClassLoader();

        Enumeration<URL> resources = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);

        FileInputStream inputStream = (FileInputStream) Resources.getResourceAsStream("mybatis.xml");
        URL resource1 = ClassLoaderPractice.class.getClassLoader().getParent().getResource("");

        try {
            //META-INF/spring.factories
            Enumeration<URL> urls = (classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) : ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            int files = 0;
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                UrlResource resource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties((Resource) resource);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String factoryTypeName = ((String) entry.getKey()).trim();
                    for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
                        result.add(factoryTypeName, factoryImplementationName.trim());
                    }
                }
                files++;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class TestClassForName {
        public static void main(String[] args) {
            try {
                System.out.println("======= begin =======");
                Class.forName("com.madm.learnroute.javaee.ClassLoaderPractice");
                System.out.println("======= end =======");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static class TestClassLoader {
        public static void main(String[] args) {
            try {
                System.out.println("======= begin =======");
                ClassLoader.getSystemClassLoader().loadClass("com.madm.learnroute.javaee.ClassLoaderPractice");
                System.out.println("======= end =======");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
