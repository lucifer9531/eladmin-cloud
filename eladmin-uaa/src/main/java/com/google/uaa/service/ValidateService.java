package com.google.uaa.service;

import com.google.core.common.api.Result;

/**
 * 验证码业务
 * @author iris
 */
public interface ValidateService {

    /**
     * 获取验证码
     * @return
     */
    Result<?> getCode();

    /**
     * 获取手机验证码
     * @param mobile
     * @return
     */
    Result<?> getSmsCode(String mobile);
}
