package com.yanan.framework.dto.orm;

import com.yanan.framework.dto.fragment.SqlFragment;

import java.sql.ResultSet;
import java.util.List;

/**
 * Orm构建接口
 * @author yanan
 *
 */
public interface OrmBuilder<T> {
	/**
	 * 构建一个list类型的数据
	 * @param resultSet 结果集合
	 * @param sqlFragment sql片段
	 * @return 对象集合
	 */
	List<Object> builder(T resultSet, SqlFragment sqlFragment);
}