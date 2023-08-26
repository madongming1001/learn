package com.madm.learnroute.technology.mybatis.mybatis.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 周瑜
 */
@Component
public class ZhouyuImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        // 扫描路径
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ZhoyuMapperScan.class.getName());
        String path = (String) annotationAttributes.get("value");


        ZhouyuBeanDefinitionScanner scanner = new ZhouyuBeanDefinitionScanner(registry);

        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        scanner.scan(path);
    }
}
