package com.google.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author iris
 */
@Getter
@AllArgsConstructor
public enum EnvTypeEnum {

    /**
     * 环境变量
     * LOCAL 本地
     * DEV 开发
     * TEST 测试
     * PROD 生产
     * DOCKER Docker
     */
    LOCAL("local"),
    DEV("dev"),
    TEST("test"),
    PROD("prod"),
    DOCKER("docker");

    private final String value;
}
