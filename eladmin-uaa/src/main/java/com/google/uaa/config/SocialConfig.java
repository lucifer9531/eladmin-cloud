package com.google.uaa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * social的配置参数
 * @author iris
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "social.value")
public class SocialConfig {

    private String url;
}
