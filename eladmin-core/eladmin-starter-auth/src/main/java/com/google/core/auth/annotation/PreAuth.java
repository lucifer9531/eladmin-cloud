package com.google.core.auth.annotation;

import java.lang.annotation.*;

/**
 * URL权限注解
 * @author iris
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {

	/**
	 * 是否启用
	 * @return boolean
	 */
	boolean enabled() default true;

	/**
	 * 验证用户是否授权
	 * @return String
	 */
	String hasPerm() default "";
}
