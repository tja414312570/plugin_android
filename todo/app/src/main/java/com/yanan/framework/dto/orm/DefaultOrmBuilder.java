package com.yanan.framework.dto.orm;

import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DefaultOrmBuilder implements OrmBuilder{
	StringBuilder stringBuffer = new StringBuilder();
	@Override
	public List<Object> builder(ResultSet resultSet, SqlFragment sqlFragment) {
		try {
			List<Object> results = new ArrayList<Object>();
			//1获取返回值类型
			Class<?> resultType = sqlFragment.getResultTypeClass();
			//2判断类型时否是List map 等聚合函数
			if(ReflectUtils.implementsOf(resultType, Map.class)){
				this.wrapperMap(resultSet, results,resultType);
			}else{
				if(ParameterUtils.isBaseType(resultType))
					while(resultSet.next())
						results.add(ParameterUtils.castType( resultSet.getObject(1), resultType));
				else
					this.wrapperBean(resultSet,results,resultType);
			}
			return results;
		} catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("failed to wrapper the result set!",e);
		}
	}
	private void wrapperBean(ResultSet resultSet, List<Object> result, Class<?> resultType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
//		DataTable tab = Class2TabMappingCache.getDBTab4Orm(resultType);
//		Iterator<DBColumn> columnIterator = tab.getDBColumns().values().iterator();
//		stringBuffer.setLength(0);
//		DBColumn[] colNameArray = new DBColumn[tab.getDBColumns().values().size()];
//		int i =0;
//		while(columnIterator.hasNext()){
//			DBColumn dbColumn = columnIterator.next();
//			colNameArray[i++] = dbColumn;
//			String columnName = dbColumn.getName();
//			if(log.isDebugEnabled()) {
//				stringBuffer.append(columnName);
//				if(columnIterator.hasNext())
//					stringBuffer.append(",");
//			}
//		}
//		if(log.isDebugEnabled())
//			log.debug(stringBuffer.toString());
//		while (resultSet.next()) {
//			//可以使用PlugsHandler代理类，实现aop。但对Gson序列化有影响
////			Object beanInstance = PlugsFactory.getPlugsInstance(resultType);
//			AppClassLoader loader = new AppClassLoader(resultType);
//			stringBuffer.setLength(0);
//			for(i=0;i< colNameArray.length;i++) {
//				DBColumn column = colNameArray[i];
//				Field field = column.getField();
//				Object object = resultSet.getObject(column.getName());
//				if(log.isDebugEnabled()) {
//					stringBuffer.append(object);
//					if(i < colNameArray.length-1)
//						stringBuffer.append(",");
//				}
//				if(object==null)
//					continue;
//				loader.set(field,ParameterUtils.castType(object,field.getType()));
//			}
//			if(log.isDebugEnabled())
//				log.debug(stringBuffer.toString());
//			result.add(loader.getLoadedObject());
//		}
	}
	private void wrapperMap(ResultSet resultSet,List<Object> results, Class<?> resultType) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		String[] colNameArray = this.getColumnName(metaData);
		stringBuffer.setLength(0);
		while (resultSet.next()) {
			Map<String,Object> map = new HashMap<String,Object>();
			for(int i = 0 ;i<colNameArray.length;i++){
				Object result = resultSet.getObject(i+1);
				map.put(colNameArray[i], result);
			}
			results.add(map);
		}
	}
	private String[] getColumnName(ResultSetMetaData metaData) throws SQLException{
		int colCount = metaData.getColumnCount();
		stringBuffer.setLength(0);
		String[] colNameArray = new String[colCount];
		for(int i = 0;i<colCount;i++) {
			colNameArray[i] = metaData.getColumnLabel(i+1);
		}
		return colNameArray;
	}
}