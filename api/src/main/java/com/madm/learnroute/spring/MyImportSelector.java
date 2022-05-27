package com.madm.learnroute.spring;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.function.Predicate;

public class MyImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //字符串必须是类的完整限定名 getbean 不能根据名字获取 只能根据类型获取
        return new String[]{"com.madm.learnroute.spring.BusinessEvent.java"};
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return ImportSelector.super.getExclusionFilter();
    }

}
