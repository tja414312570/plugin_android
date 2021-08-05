package com.yanan.framework.dto.fragment;


import com.yanan.framework.Plugin;
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.GeneralCache;
import com.yanan.framework.dto.entry.Var;
import com.yanan.framework.fieldhandler.Singleton;

@Singleton(false)
public class VarFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(Var.class,VarFragment.class);
	}
	private String id;
	//	参数列表
	@Override
	public PreparedFragment prepared(Object object) {
		Var var = (Var) this.tagSupport;
		this.id = this.sqlFragment.getId() +"."+ var.getId();
		VarFragment varFragment = GeneralCache.getCache().get(this.id);
		if(varFragment == null) {
			GeneralCache.getCache().set(id, this);
		}
		if(this.nextSet!=null)
			return this.nextSet.prepared(object);
		return new PreparedFragment();
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		super.build(wrapper);
	}
	@SuppressWarnings("unchecked")
	public PreparedFragment preparedVar(Object objects) {
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
		 if (this.childSet != null) {
			this.childSet.setSqlFragment(this.sqlFragment);
			PreparedFragment child = this.childSet.prepared(objects);
			preparedFragment.setSql(child.getSql());
			preparedFragment.addParameter(child.getArguments());
			preparedFragment.addAllVariable(child.getVariable());
		}  else {
			preparedFragment.setSql(this.preparedParameterSql(this.value, objects));
			preparedFragment.addParameter(this.parameters);
		}
		return preparedFragment;
	}
}