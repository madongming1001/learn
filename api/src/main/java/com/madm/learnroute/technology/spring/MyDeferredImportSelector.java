package com.madm.learnroute.technology.spring;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @author dongming.ma
 * @date 2023/8/14 21:02
 */
public class MyDeferredImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        System.out.println("selectImports方法执行了---->");
        return new String[0];
    }

    @Override
    public Class<? extends Group> getImportGroup() {
        System.out.println("getImportGroup");
        return MyDeferredImportSelectorGroup.class;
    }

    public static class MyDeferredImportSelectorGroup implements Group {
        private final List<Entry> imports = new ArrayList<>();

        @Override
        public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
            System.out.println("先执行 内部group类的 process方法");
        }

        @Override
        public Iterable<Entry> selectImports() {
            System.out.println("Group中的：selectImports方法");
            return imports;
        }
    }
}