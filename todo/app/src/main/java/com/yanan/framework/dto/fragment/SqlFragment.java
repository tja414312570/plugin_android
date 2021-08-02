package com.yanan.framework.dto.fragment;

import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.dto.entry.SelectorMapping;
import com.yanan.framework.dto.mapper.PreparedSql;
import com.yanan.framework.fieldhandler.Singleton;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/**
 * sal片段，用于存储动态sql语句片段
 * 
 * @author yanan
 *
 */
@Singleton(false)
public abstract class SqlFragment implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(SelectorMapping.class,ValFragment.class);
	}
	protected String id;
	protected String resultType;
	protected String parameterType;
	protected BaseMapping baseMapping;
	protected FragmentSet fragmentSet;
	protected Class<?> resultTypeClass;
	protected Class<?> parameterTypeClass;
	protected String scheml;
	
	public String getScheml() {
		return scheml;
	}

	public void setScheml(String scheml) {
		this.scheml = scheml;
	}

	// 参数列表
	protected List<String> arguments = new ArrayList<String>();

	public void addParameter(String argument) {
		if (!this.arguments.contains(argument))
			this.arguments.add(argument);
	}

	public List<String> getArguments() {
		return arguments;
	}

	public BaseMapping getBaseMapping() {
		return baseMapping;
	}

	public void setBaseMapping(BaseMapping baseMapping) {
		this.baseMapping = baseMapping;
	}

	public Class<?> getResultTypeClass() {
		return resultTypeClass;
	}

	public void setResultTypeClass(Class<?> resultTypeClass) {
		this.resultTypeClass = resultTypeClass;
	}

	public Class<?> getParameterTypeClass() {
		return parameterTypeClass;
	}

	public void setParameterTypeClass(Class<?> parameterTypeClass) {
		this.parameterTypeClass = parameterTypeClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public FragmentSet getFragmentSet() {
		return fragmentSet;
	}

	public void setFragmentSet(FragmentSet fragmentSet) {
		this.fragmentSet = fragmentSet;
	}

	public Class<?> matchClassType(String typeString, boolean isParmeter) {
		Class<?> typeClass = null;
		if (typeString != null)
			if (!typeString.contains(".")) {
				typeString = typeString.trim().toLowerCase();
				switch (typeString) {
					case "string":
						typeClass = String.class;
						break;
					case "int":
						typeClass = int.class;
						break;
					case "float":
						typeClass = float.class;
						break;
					case "long":
						typeClass = long.class;
						break;
					case "double":
						typeClass = double.class;
						break;
					case "map":
						typeClass = Map.class;
						break;
					case "list":
						typeClass = List.class;
						break;
					case "boolean":
						typeClass = boolean.class;
						break;
					case "ResultSet":
						typeClass = ResultSet.class;
						break;
					default:
						throw new SqlExecuteException(
								"Unsupported " + (isParmeter ? "parameterType" : "resultType") + " type '" + typeString
										+ "' at id '" + this.id + "'");
				}
			} else {
				try {
					typeClass = Class.forName(typeString);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new SqlExecuteException(
							"Unsupported " + (isParmeter ? "parameterType" : "resultType") + " type '" + typeString
									+ "' at id '" + this.id + "'");
				}
			}
		return typeClass;
	}

	@Override
	public void build(Object wrapper) {
		this.baseMapping = (BaseMapping) wrapper;
		this.id = this.baseMapping.getId();
		this.parameterType = this.baseMapping.getParameterType();
		this.id = this.baseMapping.getId();
		this.parameterType = this.baseMapping.getParameterType();
		this.parameterTypeClass = matchClassType(this.parameterType, true);
		this.resultType = this.baseMapping.getResultType();
		this.resultTypeClass = matchClassType(this.resultType, false);
	}


	@Override
	public PreparedFragment prepared(Object objects) {
		this.fragmentSet.setSqlFragment(this);
		return null;
	}

	public void buildListParameter(List<String> variables, List<Object> arguments2, List<?> object) {
		for (int i = 0; i < variables.size(); i++)
			arguments2.add(i < object.size() ? object.get(i) : null);
	}

	public void buildMapParameter(List<String> variables, List<Object> arguments, Map<?, ?> object) {
		for (String key : variables) {
			arguments.add(object.get(key));
		}
	}

	public List<Object> preparedParameter(Object... parameter) {
		List<Object> arguments = new ArrayList<Object>();
		if (parameter != null) {
			if (parameter.length > 1) {
				Collections.addAll(arguments, parameter);
			} else if (parameter.length == 1) {
				Object object = parameter[0];
				if (ReflectUtils.implementsOf(object.getClass(), Map.class)) {
					this.buildMapParameter(this.arguments, arguments, (Map<?, ?>) object);
				} else if (ReflectUtils.implementsOf(object.getClass(), List.class)) {
					this.buildListParameter(this.arguments, arguments, (List<?>) object);
				} else {
					arguments.add(object);
				}
			}
		}
		return arguments;
	}

	public List<Object> preparedParameter(List<String> variables, Object... parameter) {
		List<Object> arguments = new ArrayList<Object>();
		if (parameter != null&&variables.size()>0) {
			// 需要参数的数量是否与传入参数的数量相同
			if (parameter.length > 1) {
				if (SqlFragment.removeDuplicate(variables).size() <= parameter.length)
					for (int i = 0; i < variables.size(); i++){
						int pos = this.getArguments().indexOf(variables.get(i));
						arguments.add(pos>=parameter.length?null:parameter[pos]);
					}
				else
					throw new SqlExecuteException("failed to prepared parameter \"" + variables
							+ "\"because the need parameter \"" + variables.size() + "\" get the parameter \""
							+ parameter.length + "\"! at id '"
							+ this.baseMapping.getId() + "'");
			} else if (parameter.length == 1) {
				Object object = parameter[0];
				if(object==null){
					arguments.add(object);
				}else if (ReflectUtils.implementsOf(object.getClass(), Map.class)) {
					this.buildMapParameter(variables, arguments, (Map<?, ?>) object);
				} else if (ReflectUtils.implementsOf(object.getClass(), List.class)) {
					this.buildListParameter(variables, arguments, (List<?>) object);
				} else if (ParameterUtils.isBaseType(object.getClass())) {
					if (SqlFragment.removeDuplicate(variables).size() == 1)
						for(int i = 0 ;i<variables.size();i++)
							arguments.add(object);
					else
						throw new SqlExecuteException("failed to prepared parameter \"" + variables
								+ "\"because the need parameter \"" + variables.size() + "\" get the parameter \""
								+ parameter.length + "\"! at id '" + this.baseMapping.getId() + "'");
				} else {
//					AppClassLoader loader = new AppClassLoader(object);
					for (int i = 0; i < variables.size(); i++)
						try {
//							arguments.add(loader.get(variables.get(i)));
							arguments.add(ReflectUtils.getFieldValue(variables.get(i),object));
						} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
							throw new SqlExecuteException("failed to get need parameter \"" + variables.get(i)
									+ "\" at parameterType " + object.getClass() + " at id '" + this.baseMapping.getId() + "'", e);
						}
				}
			}
		}
		return arguments;
	}

	public static <T> List<T> removeDuplicate(List<T> list) {
		LinkedHashSet<T> set = new LinkedHashSet<T>(list.size());
		set.addAll(list);
		List<T> notepadList = new ArrayList<T>(list.size());
		notepadList.addAll(set);
		return notepadList;
	}

	public abstract PreparedSql getPreparedSql(Object parameter);
}