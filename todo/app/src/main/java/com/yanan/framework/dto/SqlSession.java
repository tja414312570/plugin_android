package com.yanan.framework.dto;

import java.util.List;


/**
 * sql会话即接口，未完善
 * @author yanan
 *
 */
public interface SqlSession {
	/**
	 * 查询唯一结果
	 * @param sqlId sql id
	 * @param parameters 参数
	 * @param <T> the instance type
	 * @return 查询结果
	 */
	<T> T selectOne(String sqlId,Object...parameters);
	/**
	 * 获取数据上下文
	 * @return 上下文
	 */
//	JDBContext getContext();
	/**
	 * 查询作为列表返回
	 * @param sqlId sql id
	 * @param parameters 参数
	 * @param <T> the instance type
	 * @return 结果集合
	 */
	<T> List<T> selectList(String sqlId, Object... parameters);
	/**
	 * 插入一条数据
	 * @param <T> the instance type
	 * @param sqlId sql id
	 * @param parameters 参数
	 * @return 返回结果
	 */
	<T> boolean insert(String sqlId, Object...parameters);
	/**
	 * 批量插入数据 未实现
	 * @param sqlId sql id
	 * @param parameters 参数
	 * @param <T> the instance type
	 * @return 结果集合
	 */
	<T> List<T> insertBatch(String sqlId,Object...parameters);
	/**
	 * 更新数据
	 * @param <T> the instance type
	 * @param sqlId sql id
	 * @param parameters 参数
	 * @return 结果
	 */
	<T> boolean update(String sqlId, Object...parameters);
	/**
	 * 删除数据
	 * @param sqlId sql id
 	 * @param parameters 参数
	 * @return 删除条数
	 */
	boolean delete(String sqlId, Object...parameters);

}