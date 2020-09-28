package com.jytb.logistics.interceptor;

import java.lang.annotation.*;

/**
 * Created by fulei on 2017/8/28.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface APITokenCheck {
    boolean validate() default true; //默认需要验证
}
