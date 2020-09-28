package com.jytb.logistics.interceptor;

import java.lang.annotation.*;

/**
 * Created by fulei on 2017/2/16.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface TokenCheck {

    boolean validate() default true; //默认需要验证
}
