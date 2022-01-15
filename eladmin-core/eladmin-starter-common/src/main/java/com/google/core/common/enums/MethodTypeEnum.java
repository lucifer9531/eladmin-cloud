package com.google.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author iris
 */

@Getter
@AllArgsConstructor
public enum MethodTypeEnum {

	/**
	 * 方法类型
	 * GET
	 * PUT
	 * POST
	 * DELETE
	 * OPTIONS
	 */
	GET(false),
	PUT(true),
	POST(true),
	DELETE(false),
	HEAD(false),
	OPTIONS(false);

	private final boolean hasContent;

}
