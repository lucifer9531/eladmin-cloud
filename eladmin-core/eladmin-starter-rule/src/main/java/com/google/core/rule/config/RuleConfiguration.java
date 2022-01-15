package com.google.core.rule.config;

import com.google.core.rule.service.RuleCacheService;
import com.google.core.rule.service.impl.RuleCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author iris
 */
@Configuration
public class RuleConfiguration {

    @Bean
    public RuleCacheService ruleCacheService() {
        return new RuleCacheServiceImpl();
    }
}
