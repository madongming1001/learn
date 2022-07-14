package com.madm.learnroute.javaee;

import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClassLoaderPractice {
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";

    @SneakyThrows
    public static void main(String[] args) {
        // 出发都是舍弃小数位
//        System.out.println(String.class.getClassLoader());
//        System.out.println(ClassLoaderPractice.class.getClassLoader());
//        System.out.println(7 / 2);
//        System.out.println(7 >>> 1);
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/com/madm/learnroute/javaee/ 当前同级目录下去找
        System.out.println(ClassLoaderPractice.class.getResource("META-INF/spring.factories"));
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/ 上层目录下去找
        System.out.println(ClassLoaderPractice.class.getResource("/logback-spring.xml"));
        //file:/Users/madongming/IdeaProjects/learn/api/target/classes/ 上层目录下去找
        System.out.println(ClassLoaderPractice.class.getClassLoader().getResource("META-INF/spring.factories"));
        //null 同级目录下去找
        MultiValueMap<String, String> result = result = new LinkedMultiValueMap<>();
        ClassLoader classLoader = ClassLoaderPractice.class.getClassLoader();

        Enumeration<URL> resources = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);


        try {
            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            int files = 0;
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                UrlResource resource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
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
}
