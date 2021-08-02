package com.yanan.framework.dto.mapper;

import android.util.Log;

import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.framework.dto.orm.OrmBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * sql执行类 所有sqlsession提供的方法都在此处实现
 * 
 * @author yanan
 *
 */
public class PreparedSql {
	private String sql;
	private List<Object> parameter;
	private SqlFragment sqlFragment;

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

	@SuppressWarnings("unchecked")
	public <T> T query() throws SQLException {

		try {
			Log.d("PREP_SQL","prepared sql:" + this.sql);
			Log.d("PREP_SQL","prepared parameter:" + this.parameter);
			PreparedStatement ps = null;
			Iterator<Object> collect = parameter.iterator();
			this.preparedParameter(ps, collect);
			ResultSet rs = ps.executeQuery();
			// 通过orm Builder 来组装返回结果
			OrmBuilder builder = null;
			List<Object> result = builder.builder(rs, sqlFragment);
			Log.d("PREP_SQL","result rows:" + result.size());
			rs.close();
			ps.close();
			return (T) result;
		} catch (Throwable t) {
			throw t;
		} finally {

		}
	}

	@SuppressWarnings("unchecked")
	public <T> T queryOne() throws SQLException {
		//获取事物
		try {
			Log.d("PREP_SQL","prepared sql:" + this.sql);
			Log.d("PREP_SQL","prepared parameter:" + this.parameter);
			PreparedStatement ps = null;
			Iterator<Object> collect = parameter.iterator();
			this.preparedParameter(ps, collect);
			ResultSet rs = ps.executeQuery();
			if (sqlFragment.getResultType() == null)
				throw new SqlExecuteException("Query result type is not allowed to be empty");
			// 通过orm Builder 来组装返回结果
			OrmBuilder builder = null;
			List<Object> result = builder.builder(rs, sqlFragment);
			if (result.size() > 1)
				throw new SqlExecuteException("query result rows should \"1\" but has \"" + result.size() + "\"");
			Log.d("PREP_SQL","result rows:" + result.size());
			rs.close();
			ps.close();
			return (T) (result.size() == 1 ? result.get(0) : null);
		} catch (Throwable t) {
			throw t;
		} finally {

		}

	}

	private void preparedParameter(PreparedStatement ps, Iterator<Object> collect) throws SQLException {
		int i = 0;
		while (collect.hasNext()) {
			ps.setObject(++i, collect.next());
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T insert() throws SQLException {

		try {
			Log.d("PREP_SQL","prepared sql:" + this.sql);
			Log.d("PREP_SQL","prepared parameter:" + this.parameter);
			PreparedStatement ps = null;
			Iterator<Object> collect = parameter.iterator();
			this.preparedParameter(ps, collect);
			Class<?> resultType = sqlFragment.getResultTypeClass();
			Object generatedKey = 0;
			Object result = ps.execute();
//			QueryCache.getCache().cleanDataBaseCache(this.sqlFragment.getDataBase().getName());// 清理数据库缓存
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				generatedKey = rs.getInt(1);
			rs.close();
			ps.close();
			Log.d("PREP_SQL","execute result:" + result + ",generatedKey:" + generatedKey);
			if (resultType != null) {
				if (resultType.equals(int.class) || resultType.equals(Integer.class)) {
					return (T) generatedKey;
				}
				if (resultType.equals(boolean.class) || resultType.equals(Boolean.class)) {
					return (T) result;
				}
			}
			return (T) result;
		} catch (Throwable t) {
			throw t;
		} finally {

		}
	}

	@SuppressWarnings("unchecked")
	public <T> T update() throws SQLException {

		try {
			Log.d("PREP_SQL","prepared sql:" + this.sql);
			Log.d("PREP_SQL","prepared parameter:" + this.parameter);
			PreparedStatement ps = null;
			Iterator<Object> collect = parameter.iterator();
			this.preparedParameter(ps, collect);
			Class<?> resultType = sqlFragment.getResultTypeClass();
			Object result = ps.executeUpdate();
//			QueryCache.getCache().cleanDataBaseCache(this.sqlFragment.getDataBase().getName());// 清理数据库缓存
			ps.close();
			Log.d("PREP_SQL","execute result:" + result);
			if (resultType != null) {
				if (resultType.equals(int.class) || resultType.equals(Integer.class)) {
					return (T) result;
				}
				if (resultType.equals(boolean.class) || resultType.equals(Boolean.class)) {
					return (T) ((Object) ((int) result > 0));
				}
			}
			return (T) result;
		} catch (Throwable t) {
			throw t;
		} finally {

		}
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