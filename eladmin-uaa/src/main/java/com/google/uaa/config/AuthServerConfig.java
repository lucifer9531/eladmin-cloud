package com.google.uaa.config;

import com.google.core.common.constant.Oauth2Constant;
import com.google.core.redis.core.RedisService;
import com.google.core.security.userdetails.JwtUser;
import com.google.uaa.granter.CaptchaTokenGranter;
import com.google.uaa.granter.SmsCodeTokenGranter;
import com.google.uaa.granter.SocialTokenGranter;
import com.google.uaa.service.impl.ClientDetailsServiceImpl;
import com.google.uaa.service.impl.SingleLoginTokenServices;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.*;

/**
 * 认证服务器配置中心
 * @author iris
 **/

@Order(2)
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ClientDetailsServiceImpl clientService;

    private final RedisConnectionFactory redisConnectionFactory;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RedisService redisService;

    private final AuthRequestFactory factory;

    @Value("${eladmin.uaa.isSingleLogin:false}")
    private boolean isSingleLogin = false;

    /**
     * 配置token存储到redis中
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices tokenServices = createDefaultTokenServices();
        // token增强链
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 把jwt增强，与额外信息增强加入到增强链
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);
        // 配置tokenServices参数
        addUserDetailsService(tokenServices);
        endpoints
                .tokenGranter(tokenGranter(endpoints, tokenServices))
                .tokenServices(tokenServices)
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许表单认证请求
                .allowFormAuthenticationForClients()
                // spel表达式 访问公钥端点（/auth/token_key）需要认证
                .tokenKeyAccess("isAuthenticated()")
                // spel表达式 访问令牌解析端点（/auth/check_token）需要认证
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clientService.setSelectClientDetailsSql(Oauth2Constant.SELECT_CLIENT_DETAIL_SQL);
        clientService.setFindClientDetailsSql(Oauth2Constant.FIND_CLIENT_DETAIL_SQL);
        clients.withClientDetails(clientService);
    }

    /**
     * 重点
     * 先获取已经有的五种授权，然后添加我们自己的进去
     * @param endpoints
     * @param tokenServices
     * @return TokenGranter
     */
    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints, DefaultTokenServices tokenServices) {
        ArrayList<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 短信验证码模式
        granters.add(new SmsCodeTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService));
        // 验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService));
        // 社交登录模式
        granters.add(new SocialTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService, factory));
        // 增加密码模式
        granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(granters);
    }


    /**
     * 创建默认的tokenServices
     * @return DefaultTokenServices
     */
    private DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new SingleLoginTokenServices(isSingleLogin);
        tokenServices.setTokenStore(redisTokenStore());
        // 支持刷新token
        tokenServices.setSupportRefreshToken(Boolean.TRUE);
        tokenServices.setReuseRefreshToken(Boolean.FALSE);
        tokenServices.setClientDetailsService(clientService);
        addUserDetailsService(tokenServices);
        return tokenServices;
    }

    private void addUserDetailsService(DefaultTokenServices tokenServices) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

    /**
     * jwt token增强，添加额外信息
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

                // 添加额外信息的map
                final Map<String, Object> additionMessage = new HashMap<>(6);
                // 对于客户端鉴权模式，直接返回token
                if (oAuth2Authentication.getUserAuthentication() == null) {
                    return oAuth2AccessToken;
                }
                // 获取当前登陆的用户
                JwtUser user = (JwtUser) oAuth2Authentication.getUserAuthentication().getPrincipal();
                // 如果用户不为空 则把id放入jwt token中
                if (user != null) {
                    additionMessage.put(Oauth2Constant.EL_ADMIN_USER_ID, String.valueOf(user.getId()));
                    additionMessage.put(Oauth2Constant.EL_ADMIN_USER_NAME, user.getUsername());
                    additionMessage.put(Oauth2Constant.EL_ADMIN_AVATAR, user.getAvatar());
                    additionMessage.put(Oauth2Constant.EL_ADMIN_ROLE_ID, String.valueOf(user.getRoleId()));
                    additionMessage.put(Oauth2Constant.EL_ADMIN_TYPE, user.getType());
                    additionMessage.put(Oauth2Constant.EL_ADMIN_TENANT_ID, user.getTenantId());
                }
                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
                return oAuth2AccessToken;
            }
        };
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(Oauth2Constant.SIGN_KEY);
        return jwtAccessTokenConverter;
    }
}
