package com.google.core.common.api;

/**
 * 返回码接口
 * @author iris
 */
public interface IResultCode {

    /**
     * 返回码
     * @return
     */
    int getCode();

    /**
     * 返回消息
     * @return
     */
    String getMsg();
}
