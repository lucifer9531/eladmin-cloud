package com.google.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于标识mapping.xml中不同数据库的标识
 * @author iris
 */
@Getter
@AllArgsConstructor
public enum DbTypeEnum {

    /**
     * mysql
     */
    MYSQL("mysql", "mysql"),

    /**
     * pgsql
     */
    PG_SQL("pgsql", "postgresql"),

    /**
     * oracle
     */
    ORACLE("oracle", "oracle"),

    /**
     * mssql
     */
    MS_SQL("mssql", "sqlserver");

    private final String code;

    private final String name;
}

