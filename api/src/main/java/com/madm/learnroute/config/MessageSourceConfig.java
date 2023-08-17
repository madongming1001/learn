package com.madm.learnroute.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * @author dongming.ma
 * @date 2023/8/17 17:42
 */
@Component
public class MessageSourceConfig {
    @Bean(name = "messageSource")
    public MessageSource getMessageSource() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames("message", "message_en");

        return messageSource;

    }
}
