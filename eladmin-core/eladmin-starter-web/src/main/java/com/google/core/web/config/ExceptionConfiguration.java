package com.google.core.web.config;

import com.google.core.common.factory.YamlPropertySourceFactory;
import com.google.core.web.handler.BaseExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 统一异常处理配置
 * @author xuzhanfu
 */
@Configuration
@ComponentScan(value="com.google.core.web.handler")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:eladmin-error.yml")
public class ExceptionConfiguration {

    @Bean
    public BaseExceptionHandler baseExceptionHandler(){
        return new BaseExceptionHandler();
    }
}