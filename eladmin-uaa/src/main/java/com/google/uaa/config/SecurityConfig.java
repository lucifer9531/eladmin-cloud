package com.google.uaa.config;

import com.google.core.security.config.IgnoreUrlPropsConfiguration;
import com.google.core.security.handler.JwtAuthenticationFailureHandler;
import com.google.core.security.handler.JwtAuthenticationSuccessHandler;
import com.google.uaa.service.impl.UserDetailsServiceImpl;
import com.google.uaa.sms.SmsCodeAuthenticationSecurityConfig;
import com.google.uaa.social.SocialAuthenticationSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * 安全配置中心
 * @author iris
 **/
@Order(3)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final IgnoreUrlPropsConfiguration ignoreUrlPropsConfig;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Resource
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Resource
	private SocialAuthenticationSecurityConfig socialAuthenticationSecurityConfig;

	/**
	 * 必须要定义，否则不支持grant_type=password模式
	 *
	 * @return AuthenticationManager
	 */
	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler jwtAuthenticationFailureHandler() {
		return new JwtAuthenticationFailureHandler();
	}

	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
				= http.requestMatchers().anyRequest()
				.and()
				.formLogin()
				.and()
				.apply(smsCodeAuthenticationSecurityConfig)
				.and()
				.apply(socialAuthenticationSecurityConfig)
				.and()
				.authorizeRequests();
		ignoreUrlPropsConfig.getUrls().forEach(url -> {
			config.antMatchers(url).permitAll();
		});
		ignoreUrlPropsConfig.getIgnoreSecurity().forEach(url -> {
			config.antMatchers(url).permitAll();
		});
		config
				//任何请求
				.anyRequest()
				//都需要身份认证
				.authenticated()
				//csrf跨站请求
				.and()
				.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.
				userDetailsService(userDetailsService())
				.passwordEncoder(passwordEncoder());
	}
}
