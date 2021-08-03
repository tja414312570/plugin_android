package com.yanan.framework.dto.fragment;

import com.yanan.framework.Plugin;
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.entry.Default;
import com.yanan.framework.dto.entry.ForEach;
import com.yanan.framework.fieldhandler.Singleton;
import com.yanan.framework.javascript.Bindings;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;
import com.yanan.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于处理Trim标签产生的sql片段
 * 
 * @author yanan
 *
 */
@Singleton(false)
public class ForEachFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(ForEach.class,ForEachFragment.class);
	}
	// 逻辑表达式
	private ForEach forEach;
	private List<String> args;
	private List<String> exArgs;

	@SuppressWarnings("unchecked")
	@Override
	public PreparedFragment prepared(Object objects) {
		List<Object> param = this.getParameter(objects);
		PreparedFragment preparedFragment = Plugin.createInstance(PreparedFragment.class,false);
		if (this.nextSet != null && this.childSet != null) {
			StringBuilder sb = new StringBuilder();
			// 先处理替换的字符
			sb.append(forEach.getOpen());
			for (int i = 0; i < param.size(); i++) {
				Object obj = param.get(i);
				Bindings binder = scriptEngine.createBindings();
				binder.put(this.forEach.getItem(), obj);
				binder.put(this.forEach.getIndex(),i);
				binder.put(this.forEach.getCollection(), param);
				PreparedFragment child = this.childSet.prepared(binder);
				if(!child.getSql().trim().equals("")){
					sb.append(" ").append(child.getSql()).append(" ");
					if (i + 1 < param.size())
						sb.append(forEach.getSeparator());
				}
				if(!child.getVariable().isEmpty()){
					preparedFragment.addAllVariable(child.getVariable());
				}
			}
			sb.append(forEach.getClose());
			PreparedFragment next = this.nextSet.prepared(objects);
			preparedFragment.setSql(sb.toString()+" " + next.getSql());
			preparedFragment.addAllVariable(next.getVariable());
		} else if (this.childSet != null) {
			StringBuilder sb = new StringBuilder();
			// 先处理替换的字符
			sb.append(forEach.getOpen());
			for (int i = 0; i < param.size(); i++) {
				Object obj = param.get(i);
				Bindings binder = scriptEngine.createBindings();
				binder.put(this.forEach.getItem(), obj);
				binder.put(this.forEach.getIndex(),i);
				binder.put(this.forEach.getCollection(), param);
				PreparedFragment child = this.childSet.prepared(binder);
				if(!child.getSql().trim().equals("")){
					sb.append(" ").append(child.getSql()).append(" ");
					if (i + 1 < param.size())
						sb.append(forEach.getSeparator());
				}
				if(!child.getVariable().isEmpty()){
					preparedFragment.addAllVariable(child.getVariable());
				}
			}
			sb.append(forEach.getClose());
			preparedFragment.setSql(sb.toString());
		} else {
			preparedFragment.setSql(this.value);
			preparedFragment.addAllVariable(param);
			preparedFragment.addParameter(this.parameters);
		}
		return preparedFragment;
	}

	@SuppressWarnings("rawtypes")
	private List<Object> getParameter(Object object) {
		List<Object> result = null;
		if (exArgs == null) {
			exArgs = new ArrayList<String>();
			exArgs.addAll(this.sqlFragment.getArguments());
			exArgs.removeAll(args);
		}
		if (object != null) {
			if (ReflectUtils.implementsOf(object.getClass(), Map.class)) {
				Object obj = ((Map) object).get(this.forEach.getCollection());
				result = getBySignleParameter(obj);
			} else if (ReflectUtils.implementsOf(object.getClass(), List.class)) {
				Object obj;
				if (exArgs.size() > 1) {
					int pos = this.sqlFragment.getArguments().indexOf(this.forEach.getCollection());
					obj = ((List) object).get(pos);
				} else {
					obj = ((List) object);
				}
				result = getBySignleParameter(obj);
			} else if (ParameterUtils.isBaseType(object.getClass())) {
				result = getBySignleParameter(object);
			} else {
//				AppClassLoader loader = new AppClassLoader(object);
				try {
//					Object obj = loader.get(this.forEach.getCollection());
					Object obj = ReflectUtils.getFieldValue(this.forEach.getCollection(),object);
					result = getBySignleParameter(obj);
				} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
					throw new SqlExecuteException("failed to get need parameter \"" + this.forEach.getCollection()
							+ "\" at parameterType " + object.getClass(), e);
				}
			}
		} else {
			throw new SqlExecuteException("failed to get need parameter \"" + this.forEach.getCollection() + "\"");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<Object> getBySignleParameter(Object object) {
		List<Object> results = new ArrayList<Object>();
		if (object == null || ParameterUtils.isBaseType(object.getClass())) {
			results.add(object);
		} else if (ReflectUtils.implementsOf(object.getClass(), List.class)) {
			results = (List<Object>) object;
		}
		return results;
	}

	// 构建sql片段
	@Override
	public void build(Object wrapper) {
		this.forEach = (ForEach) this.tagSupport;
		this.sqlFragment.addParameter(this.forEach.getCollection());
		super.build(wrapper);
		List<String> vars = StringUtil.find(this.value, "#{", "}", "?");
		if (vars.size() > 1) {
			this.value = vars.get(vars.size() - 1);
			for (int i = 0; i < vars.size() - 1; i++) {
				this.sqlFragment.arguments.remove(vars.get(i));
			}
		}
		vars = StringUtil.find(this.value, "${", "}");
		if (vars.size() > 1) {
			for (int i = 0; i < vars.size(); i++) {
				this.sqlFragment.arguments.remove(vars.get(i));
			}
		}
		this.args = new ArrayList<String>();
		this.args.add(this.forEach.getCollection());
		this.args.add(this.forEach.getIndex());
		this.args.add(this.forEach.getItem());
	}

}