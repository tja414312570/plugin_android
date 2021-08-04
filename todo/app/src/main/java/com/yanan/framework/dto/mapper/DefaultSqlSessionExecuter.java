package com.yanan.framework.dto.mapper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.SqlFragmentManager;
import com.yanan.framework.dto.SqlSession;
import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.framework.dto.orm.DefaultOrmBuilder;
import com.yanan.framework.dto.orm.OrmBuilder;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架默认sqlsession的实现类
 * @author yanan
 *
 */
public class DefaultSqlSessionExecuter implements SqlSession{
    private final SqlFragmentManager sqlFragmentManager;
    private final SQLiteDatabase sqLiteDatabase;

    public DefaultSqlSessionExecuter(SQLiteDatabase sqLiteDatabase, SqlFragmentManager sqlFragmentManager) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.sqlFragmentManager = sqlFragmentManager;
    }

	/**
	 * 从数据库中查询数据
	 * 除非为java基础数据类型和String，否则参数只有第一个有效，无须再mapper中定义参数类型
	 * !该查询条件表明满足该语句的数据在数据库中最多只有一条，否则会抛出异常
	 */
	@Override
	public <T> T selectOne(String sqlId, Object... parameters) {
		Object parameter = checkParams(parameters);
		SqlFragment frag = sqlFragmentManager.getSqlFragment(sqlId);
		PreparedSql pre = frag.getPreparedSql(parameter);
		try {
			return pre.queryOne();
		} catch (SQLException e) {
			throw new SqlExecuteException("faild to execute query \""+sqlId+"\"",e);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object checkParams(Object... params) {
		if(params!=null && params.length>1) {
//			for(int i = 0;i<params.length-1;i++) {
//				for(int j = i+1;j<params.length;j++) {
//					if(params[j] != null && params[i] != null && !com.yanan.utils.reflect.AppClassLoader.isBaseType(params[i].getClass()) && params[j].getClass().equals(params[i].getClass())) {
//						throw new JDBSqlExecuteException("could not build parameter map");
//					}
//				}
//			}
			Map<String,Object> paramMap = new HashMap<>();;
			for(int i = 0;i<params.length;i++) {
				if(i==0 && params[0] != null) {
					if(ReflectUtils.implementsOf(params[0].getClass(), Map.class))
					paramMap.putAll((Map)params[0]);
					if(params[i] == null ||
							ParameterUtils.isBaseType(params[0].getClass())){
						paramMap.put("parameter_"+i, params[i]);
					}else {
						Field[] fields = ReflectUtils.getAllFields(params[0].getClass());

//						AppClassLoader loader =new com.yanan.utils.reflect.
//								AppClassLoader(params[0]);
						for(Field field : fields) {
							try {
								paramMap.put(field.getName(),ReflectUtils.getFieldValue(field,params[0]));// loader.get(field));
							} catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
								throw new SqlExecuteException("failed to build param map for field "+field,e);
							}
						}

					}
				}else {
					if(params[i] == null ||
							ParameterUtils.isBaseType(params[0].getClass())){
						paramMap.put("parameter_"+i, params[i]);
					}else {
						paramMap.put(params[i].getClass().getSimpleName(), params[i]);
					}
				}
			}
			return paramMap;
		}else
			return params == null || params.length == 0 ? null:params[0];
	}
	/**
	 * 从数据库中查询结果集，需要从mapper中定义返回类型，返回类型为一个list或其实现类。
	 * 除非为java基础数据类型和String，否则参数只有第一个有效，无须再mapper中定义参数类型
	 */
	@Override
	public <T> List<T> selectList(String sqlId, Object... parameters) {
		Object parameter = checkParams(parameters);
		SqlFragment frag = sqlFragmentManager.getSqlFragment(sqlId);
		PreparedSql pre = frag.getPreparedSql(parameter);
		try {
			Log.d("PREP_SQL","prepared sql:" + pre.getSql());
			Log.d("PREP_SQL","prepared parameter:" + pre.getParameter());
			String[] args = new String[pre.getParameter().size()];
			for(int i = 0;i<pre.getParameter().size();i++){
				args[i] = String.valueOf(pre.getParameter().get(i));
			}
			Cursor cursor = this.sqLiteDatabase.rawQuery(pre.getSql(),args);
			OrmBuilder<Cursor> ormBuilder = new DefaultOrmBuilder();

			return (List<T>) ormBuilder.builder(cursor,frag);
		} catch (android.database.SQLException e) {
			throw new SqlExecuteException("faild to execute query \""+sqlId+"\" "+pre.getSql() +" params "+pre.getParameter(),e);
		}
	}
	@Override
	public <T> boolean insert(String sqlId, Object... parameters) {
		Object parameter = checkParams(parameters);
		SqlFragment frag = sqlFragmentManager.getSqlFragment(sqlId);
		PreparedSql pre = frag.getPreparedSql(parameter);
		try {
			Log.d("PREP_SQL","prepared sql:" + pre.getSql());
			Log.d("PREP_SQL","prepared parameter:" + pre.getParameter());
			this.sqLiteDatabase.execSQL(pre.getSql(),pre.getParameter().toArray());
//			Method method = this.sqLiteDatabase.getClass().getMethod("executeSql",String.class,new Object[]{}.getClass());
//			int result = (int) ReflectUtils.invokeMethod(this.sqLiteDatabase,method,pre.getSql(),pre.getParameter().toArray(new Object[]{}));
//			int result = this.sqLiteDatabase.executeSql(pre.getSql(),pre.getParameter().toArray(new Object[]{}));
			return true;
		} catch (android.database.SQLException e) {
			throw new SqlExecuteException("faild to execute query \""+sqlId+"\" "+pre.getSql() +" params "+pre.getParameter(),e);
		}
	}
	@Override
	public <T> List<T> insertBatch(String sqlId, Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> boolean update(String sqlId, Object... parameters) {
		Object parameter = checkParams(parameters);
		SqlFragment frag = sqlFragmentManager.getSqlFragment(sqlId);
		PreparedSql pre = frag.getPreparedSql(parameter);
		try {
			Log.d("PREP_SQL","prepared sql:" + pre.getSql());
			Log.d("PREP_SQL","prepared parameter:" + pre.getParameter());
			this.sqLiteDatabase.execSQL(pre.getSql(),pre.getParameter().toArray());
//			Method method = this.sqLiteDatabase.getClass().getMethod("executeSql",String.class,new Object[]{}.getClass());
//			int result = (int) ReflectUtils.invokeMethod(this.sqLiteDatabase,method,pre.getSql(),pre.getParameter().toArray(new Object[]{}));
//			int result = this.sqLiteDatabase.executeSql(pre.getSql(),pre.getParameter().toArray(new Object[]{}));
			return true;
		} catch (android.database.SQLException e) {
			throw new SqlExecuteException("faild to execute query \""+sqlId+"\" "+pre.getSql() +" params "+pre.getParameter(),e);
		}
	}
	@Override
	public boolean delete(String sqlId, Object... parameters) {
		Object parameter = checkParams(parameters);
		SqlFragment frag = sqlFragmentManager.getSqlFragment(sqlId);
		PreparedSql pre = frag.getPreparedSql(parameter);
		try {
			Log.d("PREP_SQL","prepared sql:" + pre.getSql());
			Log.d("PREP_SQL","prepared parameter:" + pre.getParameter());
			this.sqLiteDatabase.execSQL(pre.getSql(),pre.getParameter().toArray());
//			Method method = this.sqLiteDatabase.getClass().getMethod("executeSql",String.class,new Object[]{}.getClass());
//			int result = (int) ReflectUtils.invokeMethod(this.sqLiteDatabase,method,pre.getSql(),pre.getParameter().toArray(new Object[]{}));
//			int result = this.sqLiteDatabase.executeSql(pre.getSql(),pre.getParameter().toArray(new Object[]{}));
			return true;
		} catch (android.database.SQLException e) {
			throw new SqlExecuteException("faild to execute query \""+sqlId+"\" "+pre.getSql() +" params "+pre.getParameter(),e);
		}
	}

}