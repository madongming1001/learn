package com.madm.learnroute.validate;

import com.alibaba.nacos.common.utils.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author dongming.ma
 * @date 2023/8/18 18:05
 */
public class ClassNameCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String osName = context.getEnvironment().getProperty("os.name");
        if (StringUtils.equalsIgnoreCase(osName, "Mac OS X")) {
            return true;
        }
        return false;
    }
}
