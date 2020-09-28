package com.jytb.logistics.security;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Created by fulei on 2016-12-13.
 */
public class CustomMessageSource extends ResourceBundleMessageSource {

    /**
     * 指定resources下的i18n目录中的资源文件
     */
    public CustomMessageSource() {
        setBasename("i18n.messages_zh_CN");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new CustomMessageSource());
    }
}
