package com.google.core.auth.annotation;

import java.lang.annotation.*;

/**
 * 启用Token验证
 * @author iris
 */
@Deprecated
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableToken {
}
