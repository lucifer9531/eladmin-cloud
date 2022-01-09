package com.google.core.cloud.config;

import com.google.core.cloud.filter.TraceFilter;
import com.google.core.cloud.props.ElAdminRequestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author iris
 */
@Configuration
@EnableConfigurationProperties(ElAdminRequestProperties.class)
public class RequestConfiguration {

    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }
}
