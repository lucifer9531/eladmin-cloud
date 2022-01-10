package com.google.uaa.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.core.common.constant.Oauth2Constant;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Objects;

/**
 * 自定义client表，并将数据缓存到redis，处理缓存优化
 * @author iris
 */
@Setter
@Slf4j
@Service
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Bean
    @Primary
    public ClientDetailsServiceImpl clientDetailsService() {
        ClientDetailsServiceImpl clientDetailsService = new ClientDetailsServiceImpl(dataSource);
        clientDetailsService.setRedisTemplate(redisTemplate);
        return clientDetailsService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        ClientDetails clientDetails = (ClientDetails) redisTemplate.opsForValue().get(clientKey(clientId));
        if (ObjectUtil.isEmpty(clientDetails)) {
            clientDetails = getCacheClient(clientId);
        }
        Objects.requireNonNull(clientDetails).getAuthorizedGrantTypes().add(Oauth2Constant.CLIENT_CREDENTIALS);
        return clientDetails;
    }

    private ClientDetails getCacheClient(String clientId) {
        ClientDetails clientDetails = null;
        try {
            clientDetails = super.loadClientByClientId(clientId);
            if (ObjectUtil.isNotEmpty(clientDetails)) {
                redisTemplate.opsForValue().set(clientKey(clientId), clientDetails);
                log.debug("Cache clientId:{}, clientDetails:{}", clientId, clientDetails);
            }
        } catch (Exception e) {
            log.error("Exception for clientId:{}, message:{}", clientId, e.getMessage());
        }
        return clientDetails;
    }

    private String clientKey(String clientId) {
        return Oauth2Constant.CLIENT_TABLE + ":" + clientId;
    }
}
