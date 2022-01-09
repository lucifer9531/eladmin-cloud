package com.google.core.security.annotation;

import com.google.core.security.config.ResourceServerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 资源服务注解
 * @author iris
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ResourceServerConfig.class)
public @interface EnableResourceServer {
}
