package com.google.core.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author iris
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 业务异常
     */
    FAILURE(400, "业务异常"),
    /**
     * 服务未找到
     */
    NOT_FOUND(404, "服务未找到"),
    /**
     * 服务异常
     */
    ERROR(500, "服务异常");

    /**
     * 状态吗
     */
    final int code;

    /**
     * 消息内容
     */
    final String msg;
}
