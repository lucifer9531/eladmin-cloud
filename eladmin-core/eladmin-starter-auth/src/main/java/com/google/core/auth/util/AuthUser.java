package com.google.core.auth.util;

import com.google.core.common.entity.LoginUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类
 * @author iris
 */
@UtilityClass
public class AuthUser {

	/**
	 * 获取Authentication
	 */
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public LoginUser getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}


	/**
	 * 获取用户
	 *
	 * @param authentication 用户认证
	 * @return 登录用户
	 */
	public LoginUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof LoginUser) {
			return (LoginUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户名称
	 *
	 * @return username
	 */
	public String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getName();
	}
}
