package com.google.core.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtil {

	/**
	 * 获取request
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
	}
}
