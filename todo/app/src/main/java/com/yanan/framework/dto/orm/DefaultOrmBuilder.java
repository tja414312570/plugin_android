package com.yanan.framework.dto.orm;

import android.database.Cursor;

import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultOrmBuilder implements OrmBuilder<Cursor>{
	StringBuilder stringBuffer = new StringBuilder();
	@Override
	public List<Object> builder(Cursor cursor, SqlFragment sqlFragment) {
		try {
			List<Object> results = new ArrayList<Object>();
			//1获取返回值类型
			Class<?> resultType = sqlFragment.getResultTypeClass();
			//2判断类型时否是List map 等聚合函数
			if(ReflectUtils.implementsOf(resultType, Map.class)){
				this.wrapperMap(cursor, results,resultType);
			}else{
				if(ParameterUtils.isBaseType(resultType))
					this.wrapperBase(cursor, results,resultType);
				else
					this.wrapperBean(cursor,results,resultType);
			}
			return results;
		} catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | NoSuchFieldException e) {
			throw new RuntimeException("failed to wrapper the result set!",e);
		}
	}
	private void wrapperBean(Cursor cursor, List<Object> results, Class<?> resultType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException, InstantiationException, NoSuchFieldException {
		while(cursor.moveToNext()){
			Object instance = resultType.newInstance();
			for(int i = 0;i<cursor.getColumnCount();i++){
				String columnName = cursor.getColumnName(i);
				int type = cursor.getType(i);
				Object value = null;
				switch (type){
					case Cursor.FIELD_TYPE_BLOB:
						value = cursor.getBlob(i);
						break;
					case Cursor.FIELD_TYPE_FLOAT:
						value = cursor.getFloat(i);
						break;
					case Cursor.FIELD_TYPE_INTEGER:
						value = cursor.getInt(i);
						break;
					case Cursor.FIELD_TYPE_STRING:
						value = cursor.getString(i);
						break;
				}
				ReflectUtils.setFieldValue(columnName,instance,ParameterUtils.castType(value,resultType));
			}
			results.add(instance);
		}
	}
	private void wrapperMap(Cursor cursor,List<Object> results, Class<?> resultType) throws SQLException {
		while(cursor.moveToNext()){
			Map item = new HashMap();
			for(int i = 0;i<cursor.getColumnCount();i++){
				String columnName = cursor.getColumnName(i);
				int type = cursor.getType(i);
				Object value = null;
				switch (type){
					case Cursor.FIELD_TYPE_BLOB:
						value = cursor.getBlob(i);
						break;
					case Cursor.FIELD_TYPE_FLOAT:
						value = cursor.getFloat(i);
						break;
					case Cursor.FIELD_TYPE_INTEGER:
						value = cursor.getInt(i);
						break;
					case Cursor.FIELD_TYPE_STRING:
						value = cursor.getString(i);
						break;
				}
				item.put(columnName,ParameterUtils.castType(value,resultType));
			}
			results.add(item);
		}
	}
	private void wrapperBase(Cursor cursor,List<Object> results, Class<?> resultType) throws SQLException {
		while(cursor.moveToNext()){
			Object value = null;
				int type = cursor.getType(0);
				switch (type){
					case Cursor.FIELD_TYPE_BLOB:
						value = cursor.getBlob(0);
						break;
					case Cursor.FIELD_TYPE_FLOAT:
						value = cursor.getFloat(0);
						break;
					case Cursor.FIELD_TYPE_INTEGER:
						value = cursor.getInt(0);
						break;
					case Cursor.FIELD_TYPE_STRING:
						value = cursor.getString(0);
						break;
				}
			results.add(value);
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