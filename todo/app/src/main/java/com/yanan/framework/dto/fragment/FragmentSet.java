package com.yanan.framework.dto.fragment;

import com.yanan.framework.Plugin;
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.SqlFragmentManager;
import com.yanan.framework.dto.entry.Default;
import com.yanan.framework.dto.entry.TagSupport;
import com.yanan.framework.fieldhandler.Singleton;
import com.yanan.framework.javascript.Bindings;
import com.yanan.framework.javascript.ScriptEngine;
import com.yanan.framework.javascript.ScriptEngineManager;
import com.yanan.framework.javascript.ScriptException;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;
import com.yanan.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * sql片断的默认集合实现，基于二叉树结构
 * 当前片断仅包含本节点信息以及子节点和下一节点信息
 * 条件判断使用jdk的Nashorn引擎（javascript引擎）
 * @author yanan
 *
 */
@Singleton(false)
public class FragmentSet implements FragmentBuilder {
//	static {
//		DtoContext.registerFragmentSet(Default.class,FragmentSet.class);
//	}
	// xml文档
	protected String xml;
	// sql语句
	protected String value;
	// 子片段的集合
	protected FragmentSet childSet;
	// 下一个片段的集合
	protected FragmentSet nextSet;
	protected TagSupport tagSupport;
	protected SqlFragment sqlFragment;
	protected SqlFragmentManager sqlFragmentManager;
	protected List<String> parameters = new ArrayList<String>();
	ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("Rhino");
	public TagSupport getTagSupport() {
		return tagSupport;
	}
	
	public void setTagSupport(TagSupport tagSupport) {
		this.tagSupport = tagSupport;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FragmentSet getChildSet() {
		return childSet;
	}

	public void setChildSet(FragmentSet childSet) {
		this.childSet = childSet;
	}
	
	/**
	 * 根据参数将SQL片段（FragmentSet）生成具有语义的预执行片段(PreparedFragment)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PreparedFragment prepared(Object objects) {
		/**
		 * 通过调用PlugsFactory生成PreparedFragment的实例。
		 */
		PreparedFragment preparedFragment = Plugin.createInstance(PreparedFragment.class,false);
		/**
		 * 将所有的变量都添加到预执行片段中
		 */
		preparedFragment.addAllVariable(this.preparedParameter(this.parameters, objects));
		/**
		 * if next node is not null and child node is not null
		 */
		if (this.nextSet != null && this.childSet != null) {
			this.childSet.setSqlFragment(this.sqlFragment);
			this.nextSet.setSqlFragment(this.sqlFragment);
			/**
			 * invoke child node's prepared method to build child node's PreparedFragment object;
			 */
			PreparedFragment child = this.childSet.prepared(objects);
			/**
			 * invoke next node's prepared method to build next node's PreparedFragment object;
			 */
			PreparedFragment next = this.nextSet.prepared(objects);
			/**
			 * build child and next prparedFragmet SQL as this fragment SQL.
			 */
			preparedFragment.setSql(child.getSql()+" " + next.getSql());
			/**
			 * add child and next node all argument to this fragment
			 */
			preparedFragment.addParameter(child.getArguments(), next.getArguments());
			/**
			 * add child and next node all variable to this fragment
			 */
			preparedFragment.addAllVariable(child.getVariable());
			preparedFragment.addAllVariable(next.getVariable());
		} else if (this.childSet != null) {
			this.childSet.setSqlFragment(this.sqlFragment);
			PreparedFragment child = this.childSet.prepared(objects);
			preparedFragment.setSql(child.getSql());
			preparedFragment.addParameter(child.getArguments());
			preparedFragment.addAllVariable(child.getVariable());
		} else if (this.nextSet != null) {
			this.nextSet.setSqlFragment(this.sqlFragment);
			PreparedFragment next = this.nextSet.prepared(objects);
			preparedFragment.setSql(this.preparedParameterSql(this.value, objects).trim()+ " "+next.getSql());
			preparedFragment.addParameter(this.parameters, next.getArguments());
			preparedFragment.addAllVariable(next.getVariable());
		} else {
			preparedFragment.setSql(this.preparedParameterSql(this.value, objects));
			preparedFragment.addParameter(this.parameters);
		}
		return preparedFragment;
	}
	/**
	 * 此方法用于将sql语句中的${|args}代替为具体的变量
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public String preparedParameterSql(String sql, Object parameter) {
		List<String> variable = StringUtil.find(sql, "${", "}");
		if (variable.size() > 0) {
			StringBuffer sb = new StringBuffer(sql).append(" ");
			List<Object> arguments = this.preparedParameter(variable, parameter);
			for (int i = 0; i < variable.size(); i++) {
				String rep = "${" + variable.get(i) + "}";
				int index = sb.indexOf(rep);
				Object arg = arguments.get(i);
				while (index > -1) {
					//不提供变量自动加上引号
					sb = new StringBuffer(sb.substring(0, index)).append(arg)//.append("'").append(arg).append("'")
							.append(sb.substring(index + rep.length()));
					index = sb.indexOf(rep);
				}
			}
			sql = sb.toString();
		}
		//将制表符和换行符替换为空格。
		sql = sql.replaceAll("\n\t\t", " ").replaceAll("\t\t", " ").replaceAll("\t", " ").replaceAll("\n", " ").trim();
		return sql;
	}

	/**
	 * 获取创建sql时所涉及到的参数列表
	 * @param variables 此片段锁涉及到的变量
	 * @param parameter 调用接口传入的参数
	 * @return 集合
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Object> preparedParameter(List<String> variables, Object parameter) {
		List<Object> arguments = new ArrayList<Object>();
		if (parameter != null && variables.size() > 0) {
			if(parameter instanceof Bindings){
					for(int i = 0;i<variables.size();i++){
						try {
							arguments.add(scriptEngine.eval(variables.get(i),(Bindings) parameter));
						} catch (ScriptException e) {
							throw new SqlExecuteException("failed to execute \"" + variables.get(i) + "\" expression! at id '" + this.sqlFragment.baseMapping.getId()
									+ "' at item data " + parameter, e);
						}
					}
				}else if (ParameterUtils.isBaseType(parameter.getClass())) {
					if (SqlFragment.removeDuplicate(variables).size() == 1)
						for (int i = 0; i < variables.size(); i++)
							arguments.add(parameter);
					else
						throw new SqlExecuteException("failed to prepared parameter \"" + variables
								+ "\"because the parameter size at last \""+variables.size()+"\"! at id '" + this.sqlFragment.baseMapping.getId() + "'");
				} else {
					//如果所需参数和准备的参数都只有一个时，直接赋值
					if(variables.size() == 1 && ReflectUtils.implementsOf(parameter.getClass(), Map.class) && ((Map) parameter).size() == 1) {
							arguments.add(((Map) parameter).values().iterator().next());
					}else {
						for (String key : variables) {
							Object value = this.decodeParameter(key, parameter);
							arguments.add(value);
						}
					}
				}
			}
		return arguments;
	}

	@SuppressWarnings("unchecked")
	public Object decodeParameter(String parameterName,Object parameter) {
		if(parameter == null)
			return null;
		//如果是Map集合
		String express;
		Object value;
		if(ReflectUtils.implementsOf(parameter.getClass(), Map.class)) {
			Map<String,Object> parameterMap = (Map<String,Object>) parameter;
			value = parameterMap.get(parameterName);
			if(value != null) {
				return value;
			}else {
				int offset = parameterName.indexOf(".");
				if(offset>-1) {
					express = parameterName.substring(0,offset);
					value = parameterMap.get(express);
					if(value != null){
						express =  parameterName.substring(offset+1);
						return decodeParameter(express,value);
					}
				}
				return value;
			}
		}else {
//			AppClassLoader parameterLoader = new AppClassLoader(parameter);
//			String header = parameterLoader.getLoadedClass().getSimpleName()+".";
			String header = parameter.getClass().getSimpleName()+".";
				if(parameterName.startsWith(header)) {
					parameterName = parameterName.substring(header.length()); 
				}
				try {
					int offset = parameterName.indexOf(".");
					if(offset>-1) {
						express = parameterName.substring(0,offset);
//						value = parameterLoader.get(express);
						value = ReflectUtils.getFieldValue(express,parameter);
						if(value != null){
							express =  parameterName.substring(offset+1);
							return decodeParameter(express,value);
						}else {
							return null;
						}
					}else {
						return get(parameter,parameterName);
					}
				} catch ( Exception e) {
					throw new SqlExecuteException("failed to get parameter \"" + parameterName
							+ "\" at parameterType " + parameter.getClass() + " at id '"
							+ this.sqlFragment.baseMapping.getId() + "'", e);
				}
		}
	}
	public Object get(Object instance,String field) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		if(instance == null)
			return null;
		if(instance instanceof Map){
			return ((Map)instance).get(field);
		}
		return ReflectUtils.getFieldValue(field,instance);
	}
	@Override
	public void build(Object wrapper) {
		String sql = this.xml;
		// 去掉外面的标签,并重组sql语句,不能删除空格
		int index = sql.indexOf(SPLITPREFIX);
		int endex = sql.lastIndexOf(SUFFIX);
		if (endex != -1 && index != -1 && this.tagSupport != null) {
			sql = sql.substring(index + 1, endex);
			// 简历一个临时的FragmentSet
			FragmentSet currentFragmentSet;
			FragmentSet preFragmentSet = null;
			// 分隔片段
			// 获取sql里的动态语句标签//并对最外层分隔
			List<TagSupport> tags = this.tagSupport.getTags();
			if(tags != null)
			for (TagSupport tag : tags) {
				// 获得TagSupport的类型
				Class<?> tagClass = tag.getClass();
				// 截取类容
				int predex = sql.indexOf(tag.getXml());
				int len = tag.getXml().length();
				String preffix = sql.substring(0, predex);
				if (!preffix.trim().equals("")) {
					currentFragmentSet = Plugin.createInstance(DtoContext.getFragmentSet(Default.class),false);
					currentFragmentSet.setXml(preffix);
					currentFragmentSet.setValue(preffix);
					currentFragmentSet.setSqlFragment(this.sqlFragment);
					currentFragmentSet.build(null);
					if (this.childSet == null)
						this.childSet = currentFragmentSet;
					if (preFragmentSet != null)
						preFragmentSet.setNextSet(currentFragmentSet);
					preFragmentSet = currentFragmentSet;
				}
				// 根据类型获取对应FragmentSet
//				currentFragmentSet = (FragmentSet) PlugsFactory.getPluginsInstanceByAttributeStrict(FragmentBuilder.class,
//						tagClass.getName() + ".fragment");
				currentFragmentSet = Plugin.createInstance(DtoContext.getFragmentSet(tagClass),false);
				// 判断根FragmentSet是否为空
				if (this.childSet == null)
					this.childSet = currentFragmentSet;
				if (preFragmentSet != null)
					preFragmentSet.setNextSet(currentFragmentSet);
				preFragmentSet = currentFragmentSet;
				currentFragmentSet.setXml(tag.getXml());
				currentFragmentSet.setValue(tag.getValue());
				currentFragmentSet.setTagSupport(tag);
				currentFragmentSet.setSqlFragment(this.sqlFragment);
				currentFragmentSet.build(tag.getTags());
				sql = sql.substring(predex + len);
			}
			if (!sql.trim().equals("")) {
				currentFragmentSet = Plugin.createInstance(DtoContext.getFragmentSet(Default.class),false);
				currentFragmentSet.setXml(sql);
				currentFragmentSet.setValue(sql);
				currentFragmentSet.setSqlFragment(this.sqlFragment);
				currentFragmentSet.build(null);
				if (this.childSet == null)
					this.childSet = currentFragmentSet;
				if (preFragmentSet != null)
					preFragmentSet.setNextSet(currentFragmentSet);
				preFragmentSet = currentFragmentSet;
			}
		}

		List<String> tempParams = new ArrayList<String>();
		String sqlTmp = this.value;
		if (this.childSet != null)
			sqlTmp = sqlTmp.replace(this.childSet.getXml(), "");
		List<String> vars = StringUtil.find(sqlTmp, "#{", "}", "?");
		String tempValue = this.value;
		if (vars.size() > 1) {
			this.value = vars.get(vars.size() - 1);
			for (int i = 0; i < vars.size() - 1; i++) {
				this.parameters.add(vars.get(i));
				tempParams.add(vars.get(i));
			}
		}
		vars = StringUtil.find(sqlTmp, "${", "}");
		if (vars.size() > 0) {
			tempParams.addAll(vars);
		}
		// 重组Sql参数集合
		if (tempParams.size() != 0) {
			Map<Integer, String> treeMap = new TreeMap<Integer, String>();
			for (String var : tempParams) {
				if (!treeMap.containsValue(var)) {
					index = tempValue.indexOf(var);
					treeMap.put(index, var);
				}
			}
			for (String arg : treeMap.values()) {
				if (!arg.contains(".") && !arg.contains("[")) {
					this.sqlFragment.addParameter(arg);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean test(String express, List<String> argument, Object object) {
		Bindings binder = scriptEngine.createBindings();
		if (object != null) {
			if(ReflectUtils.implementsOf(object.getClass(), Bindings.class)){ 
				binder = (Bindings) object;
			}
			// 如果参数类型为Map
			else if (ReflectUtils.implementsOf(object.getClass(), Map.class)) {
				binder.putAll((Map<? extends String, ?>) object);
				for(String key : argument) {
					if(!binder.containsKey(key))
						binder.put(key, null);
				}
				// 如果参数为List
			} else if (ReflectUtils.implementsOf(object.getClass(), List.class)) {
				this.buldListBinder(binder, argument, (List<?>) object);
				// 如果参数时基本类型
			} else if (ParameterUtils.isBaseType(object.getClass())) {
				if (argument.size() == 1){
					binder.put(argument.get(0), object);
				}
				else
					throw new SqlExecuteException(
							"failed to execute \"" + express + "\" expression because the need parameter \""
									+ argument + "\" but found one! at id '" + this.sqlFragment.baseMapping.getId() + "'");
			} else {
//				AppClassLoader loader = new AppClassLoader(object);
				for (int i = 0; i < argument.size(); i++)
					try {
//						binder.put(argument.get(i), loader.get(argument.get(i)));
						binder.put(argument.get(i), ReflectUtils.getFieldValue(argument.get(i),object));
					} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
						throw new SqlExecuteException("failed to get need parameter \"" + argument.get(i)
								+ "\" at express \"" + express + "\" at parameterType " + object.getClass(),
								e);
					}
			}
			} else {
				for (int i = 0; i < argument.size(); i++)
					binder.put(argument.get(i), null);
			}
		Object result = null;
		try {
			result = scriptEngine.eval(express, binder);
			if (result.getClass().equals(Boolean.class))
				return (boolean) result;
			else
				throw new SqlExecuteException("failed to execute \"" + express
						+ "\" expression,because the result type is not boolean! at mapping id '" + this.sqlFragment.baseMapping.getId()
						+ "'");
		} catch (ScriptException e) {
			throw new SqlExecuteException("failed to execute \"" + express + "\" expression! at mapping id '" + this.sqlFragment.baseMapping.getId()
					+ "'", e);
		}
	}

//	public Object eval(Map<String, Object> binds, String variable) {
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine engine = manager.getEngineByName("JavaScript");
//		Bindings bind = engine.createBindings();
//		bind.putAll(binds);
//		try {
//			return engine.eval("stu.getAid()", bind);
//		} catch (ScriptException e) {
//			throw new SqlExecuteException("failed to execute \"" + variable + "\" expression! at mapping file '"
//					+ this.sqlFragment.baseMapping.getXmlFile() + "' at id '" + this.sqlFragment.baseMapping.getId()
//					+ "' at item data " + binds, e);
//		}
//	}

	private void buldListBinder(Bindings binder, List<String> argument, List<?> object) {
		for (int i = 0; i < argument.size(); i++)
			binder.put(argument.get(i), i < object.size() ? object.get(i) : null);
	}
	
	/**
	 * 将表达式中的and or not 转化为JS可以执行的逻辑符
	 * @param test
	 * @return
	 */
	public String switchExpress(String test) {
		test = test.replace(" and ", " && ").replace(" or ", " || ").replace(" not ", " ! ");
		return test;
	}

	public FragmentSet getNextSet() {
		return nextSet;
	}

	public void setNextSet(FragmentSet nextSet) {
		this.nextSet = nextSet;
	}

	public SqlFragment getSqlFragment() {
		return sqlFragment;
	}

	public void setSqlFragment(SqlFragment sqlFragment) {
		this.sqlFragment = sqlFragment;
	}

}