package com.yanan.framework.dto.mapper;

import com.yanan.framework.dto.fragment.SqlFragment;

import java.util.List;

/**
 * sql执行类 所有sqlsession提供的方法都在此处实现
 * 
 * @author yanan
 *
 */
public class PreparedSql {
	private final String sql;
	private final List<Object> parameter;
	private final SqlFragment sqlFragment;

	@Override
	public String toString() {
		return "PreparedSql [sql=" + sql + ", parameter=" + parameter + ", sqlFragment=" + sqlFragment + "]";
	}

	public PreparedSql(String sql, List<Object> parameter, SqlFragment sqlFragment) {
		super();
		this.sql = sql;
		this.parameter = parameter;
		this.sqlFragment = sqlFragment;
	}
	public String getSql() {
		return sql;
	}

	public List<Object> getParameter() {
		return parameter;
	}

	public SqlFragment getSqlFragment() {
		return sqlFragment;
	}
}