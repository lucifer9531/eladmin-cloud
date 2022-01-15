package com.google.core.mybatis.injector.methods;

import com.google.core.mybatis.injector.SqlMethod;

/**
 * 插入一条数据（选择字段插入）插入如果中已经存在相同的记录，则忽略当前新数据
 * @author iris
 */
public class InsertIgnoreBatch extends AbstractInsertBatch {
	private static final String SQL_METHOD = "insertIgnoreBatch";

	public InsertIgnoreBatch() {
		super(SqlMethod.INSERT_IGNORE_ONE.getSql(), SQL_METHOD);
	}
}
