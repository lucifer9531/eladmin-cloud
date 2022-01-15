package com.google.gateway.config;

import com.google.core.cloud.props.ElAdminApiProperties;
import com.google.core.cloud.props.ElAdminRequestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author iris
 */
@Configuration
@EnableConfigurationProperties({ElAdminRequestProperties.class, ElAdminApiProperties.class})
public class PreRequestConfig {
}
